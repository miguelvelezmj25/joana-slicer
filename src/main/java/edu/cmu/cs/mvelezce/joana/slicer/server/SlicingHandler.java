package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.cmu.cs.mvelezce.joana.slicer.chop.Chopper;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedSet;

public class SlicingHandler implements HttpHandler {

  private static final String CHOPPING_ALGO = Chopper.FIXED_POINT_CHOPPER_ALGO;

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

    int sourceNode = 6;
    int targetNode = 22;
    Chopper chopper =
        new Chopper(this.programName, this.sdg, sourceNode, targetNode, CHOPPING_ALGO);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);

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

    byte[] response = new JSONObject().put("data", data).toString().getBytes();
    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
    httpExchange.getResponseBody().write(response);
    httpExchange.close();
  }
}
