package edu.cmu.cs.mvelezce.joana.slicer.chop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.cmu.cs.mvelezce.joana.slicer.data.ChopData;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.chopper.barrier.SimpleThreadBarrierChopper;
import edu.kit.joana.ifc.sdg.graph.chopper.conc.ContextSensitiveThreadChopper;
import edu.kit.joana.util.SourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Chopper {

  public static final String HOME_DIR = System.getProperty("user.home");
  public static final String ROOT_DIR = "./src/main/resources/chops/";
  public static final String DOT_JSON = ".json";

  public static final String CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO =
      "ContextSensitiveThreadChopper";
  public static final String SIMPLE_THREAD_BARRIER_CHOPPER_ALGO = "SimpleThreadBarrierChopper";

  private static final Comparator<Lines> LINES_COMPARATOR =
      Comparator.comparingInt(Lines::getStartLineNumber).thenComparing(Lines::getEndLineNumber);

  private final String programName;
  private final SDG sdg;
  private final int sourceNode;
  private final int targetNode;
  private final String algo;

  public Chopper(String programName) {
    this(programName, null, -1, -1, "");
  }

  public Chopper(String programName, SDG sdg, int sourceNode, int targetNode, String algo) {
    this.programName = programName;
    this.sdg = sdg;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.algo = algo;
  }

  public static Set<ChopData> parseChopData(Collection<SDGNode> nodes) {
    Set<ChopData> chopDataSet = new HashSet<>();
    for (SDGNode node : nodes) {
      SourceLocation sourceLocation = node.getSourceLocation();
      ChopData chopData =
          new ChopData(
              sourceLocation.getSourceFile(),
              sourceLocation.getStartRow(),
              sourceLocation.getEndRow());
      chopDataSet.add(chopData);
    }
    return chopDataSet;
  }

  public static Map<String, SortedSet<Lines>> parseFilesToLines(Set<ChopData> chopDataSet) {
    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (ChopData chopData : chopDataSet) {
      filesToLines.put(chopData.getFileName(), new TreeSet<>(LINES_COMPARATOR));
    }
    for (ChopData chopData : chopDataSet) {
      Lines lines = new Lines(chopData.getStartLineNumber(), chopData.getEndLineNumber());
      filesToLines.get(chopData.getFileName()).add(lines);
    }
    return filesToLines;
  }

  public Map<String, SortedSet<Lines>> chopAndProcess() {
    Collection<SDGNode> chop = this.chop();
    Set<ChopData> chopDataSet = Chopper.parseChopData(chop);
    return Chopper.parseFilesToLines(chopDataSet);
  }

  public Collection<SDGNode> chop() {
    long start = System.currentTimeMillis();
    edu.kit.joana.ifc.sdg.graph.chopper.Chopper chopper = this.getAlgo();
    Collection<SDGNode> chop =
        chopper.chop(this.sdg.getNode(this.sourceNode), this.sdg.getNode(this.targetNode));
    long end = System.currentTimeMillis();
    System.out.println("Chopped in: " + ((end - start) / 1E3) + " seconds");
    return chop;
  }

  private edu.kit.joana.ifc.sdg.graph.chopper.Chopper getAlgo() {
    if (this.algo.equals(CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO)) {
      return new ContextSensitiveThreadChopper(this.sdg);
    }
    if (this.algo.equals(SIMPLE_THREAD_BARRIER_CHOPPER_ALGO)) {
      return new SimpleThreadBarrierChopper(this.sdg);
    }
    throw new RuntimeException("Do not know chopping algorithm " + this.algo);
  }

  public void saveFilesToLines(Map<String, SortedSet<Lines>> filesToLines) throws IOException {
    String outputFile = ROOT_DIR + this.programName + "/" + this.programName + DOT_JSON;
    File file = new File(outputFile);
    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
      throw new RuntimeException("Could not create parent dirs for " + outputFile);
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(file, filesToLines);
  }

  public Map<String, SortedSet<Lines>> readFilesToLines() throws IOException {
    File dir = new File(ROOT_DIR + this.programName + "/");
    Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);
    if (files.size() != 1) {
      throw new RuntimeException("Expected to get a single file at " + dir);
    }

    File filesToLinesFile = files.iterator().next();
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Set<Lines>> filesToUnsortedLines =
        mapper.readValue(filesToLinesFile, new TypeReference<Map<String, Set<Lines>>>() {});

    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (String file : filesToUnsortedLines.keySet()) {
      filesToLines.put(file, new TreeSet<>(LINES_COMPARATOR));
    }
    for (Map.Entry<String, Set<Lines>> entry : filesToUnsortedLines.entrySet()) {
      filesToLines.get(entry.getKey()).addAll(entry.getValue());
    }
    return filesToLines;
  }
}
