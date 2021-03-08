package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.cmu.cs.mvelezce.joana.slicer.chop.Chopper;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.util.SourceLocation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlicingHandler implements HttpHandler {

  private static final String CHOPPING_ALGO = Chopper.FIXED_POINT_CHOPPER_ALGO;
  private static final Set<SDGNode.Kind> SOURCE_KINDS_TO_CONSIDER =
      Stream.of(SDGNode.Kind.ACTUAL_IN).collect(Collectors.toCollection(HashSet::new));
  private static final Set<SDGNode.Kind> TARGET_KINDS_TO_IGNORE = new HashSet<>();

  private final String programName;
  private final SDG sdg;

  public SlicingHandler(String programName) throws IOException {
    this.programName = programName;
    SDGReader reader = new SDGReader(this.programName);
    this.sdg = reader.readSDG();
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    JSONObject json = new JSONObject();
    try (InputStreamReader inputStreamReader =
        new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8)) {
      try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
        int b;
        StringBuilder buffer = new StringBuilder(512);
        while ((b = bufferedReader.read()) != -1) {
          buffer.append((char) b);
        }

        json = new JSONObject(buffer.toString());
        System.out.println("json object received: " + json);
      }
    }

    if (json.isEmpty()) {
      System.out.println("json object is empty");
      json = new JSONObject();
      json.put("data", "error");
      byte[] response = json.toString().getBytes();
      httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
      httpExchange.getResponseBody().write(response);
      httpExchange.close();
      System.out.println("responded with error data");
      return;
    }

    String sourceClass = json.getString("sourceClass");
    Set<Integer> sourceLines = new HashSet<>();
    for (Object line : json.getJSONArray("sourceLines").toList()) {
      sourceLines.add((int) line);
    }
    String targetClass = json.getString("targetClass");
    int targetLine = json.getInt("targetLines");

    Set<Integer> targetNodes =
        this.getTargetSDGNodeIds(targetClass, targetLine, TARGET_KINDS_TO_IGNORE);
    System.out.println("targetNodes " + targetNodes);
    System.out.println();

    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (int sourceLine : sourceLines) {
      Set<Integer> sourceNodes =
          this.getSourceSDGNodeIds(sourceClass, sourceLine, SOURCE_KINDS_TO_CONSIDER);
      System.out.println("sourceNodes: " + sourceNodes + "\n");
      for (int sourceNode : sourceNodes) {
        for (int targetNode : targetNodes) {
          System.out.println(
              "############### Source node: " + sourceNode + " - Target node: " + targetNode);
          Chopper chopper =
              new Chopper(this.programName, this.sdg, sourceNode, targetNode, CHOPPING_ALGO);
          Map<String, SortedSet<Lines>> results = chopper.chopAndProcess();
          for (Map.Entry<String, SortedSet<Lines>> entry : results.entrySet()) {
            filesToLines.putIfAbsent(entry.getKey(), new TreeSet<>(Chopper.LINES_COMPARATOR));
            filesToLines.get(entry.getKey()).addAll(entry.getValue());
          }

          chopper.saveFilesToLines(filesToLines);
          System.out.println();
        }
      }
    }

    JSONArray data = new JSONArray();
    for (Map.Entry<String, SortedSet<Lines>> entry : filesToLines.entrySet()) {
      JSONArray linesToHighlight = new JSONArray();
      for (Lines lines : entry.getValue()) {
        JSONObject line = new JSONObject();
        line.put("line", lines.getStartLineNumber());
        linesToHighlight.put(lines.getStartLineNumber());
      }
      JSONObject fileData = new JSONObject();
      fileData.put("file", entry.getKey());
      fileData.put("lines", linesToHighlight);
      data.put(fileData);
    }

    String dataToSend = new JSONObject().put("data", data).toString();
    byte[] response = dataToSend.getBytes();
    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
    httpExchange.getResponseBody().write(response);
    httpExchange.close();

    for (Map.Entry<String, SortedSet<Lines>> entry : filesToLines.entrySet()) {
      System.out.println(entry.getKey() + " - " + entry.getValue());
    }

    System.gc();
    System.out.println("\n\n\n\n");
  }

  private Set<Integer> getSourceSDGNodeIds(
      String className, int lineNumber, Set<SDGNode.Kind> kindsToConsider) {
    Set<Integer> nodes = new HashSet<>();
    for (SDGNode sdgNode : this.sdg.vertexSet()) {
      if (!kindsToConsider.contains(sdgNode.getKind())) {
        continue;
      }
      SourceLocation sourceLocation = sdgNode.getSourceLocation();
      if (sourceLocation.getSourceFile().equals(className)
          && sourceLocation.getStartRow() == lineNumber) {
        nodes.add(sdgNode.getId());
      }
    }
    return nodes;
  }

  private Set<Integer> getTargetSDGNodeIds(
      String className, int lineNumber, Set<SDGNode.Kind> kindsToIgnore) {
    Set<Integer> nodes = new HashSet<>();
    for (SDGNode sdgNode : this.sdg.vertexSet()) {
      if (kindsToIgnore.contains(sdgNode.getKind())) {
        continue;
      }
      SourceLocation sourceLocation = sdgNode.getSourceLocation();
      if (sourceLocation.getSourceFile().equals(className)
          && sourceLocation.getStartRow() == lineNumber) {
        nodes.add(sdgNode.getId());
      }
    }
    return nodes;
  }
}
