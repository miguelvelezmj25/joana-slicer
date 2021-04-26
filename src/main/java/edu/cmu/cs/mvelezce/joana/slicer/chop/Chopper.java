package edu.cmu.cs.mvelezce.joana.slicer.chop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.cmu.cs.mvelezce.joana.slicer.data.ChopData;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGEdge;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.chopper.*;
import edu.kit.joana.ifc.sdg.graph.chopper.barrier.*;
import edu.kit.joana.ifc.sdg.graph.chopper.conc.*;
import edu.kit.joana.util.SourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Chopper {

  public static final String HOME_DIR = System.getProperty("user.home");
  public static final String ROOT_DIR = "./src/main/resources/chops/";
  public static final String DOT_JSON = ".json";

  public static final String ALMOST_TIME_SENSITIVE_THREAD_CHOPPER_ALGO =
      "AlmostTimeSensitiveThreadChopper";
  public static final String CONTEXT_BASED_CHOPPER_ALGO = "ContextBasedChopper";
  public static final String CONTEXT_INSENSITIVE_CHOPPER_ALGO = "ContextInsensitiveChopper";
  public static final String CONTEXT_SENSITIVE_CHOPPER_ALGO = "ContextSensitiveChopper";
  public static final String CONTEXT_SENSITIVE_THREAD_BARRIER_CHOPPER_ALGO =
      "ContextSensitiveThreadBarrierChopper";
  public static final String CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO =
      "ContextSensitiveThreadChopper";
  public static final String FIXED_POINT_CHOPPER_ALGO = "FixedPointChopper";
  public static final String FIXED_POINT_CHOPPER_CONC_ALGO = "FixedPointChopperConc";
  public static final String INSENSITIVE_INTERSECTION_CHOPPER_ALGO =
      "InsensitiveIntersectionChopper";
  public static final String INTERSECTION_CHOPPER_ALGO = "IntersectionChopper";
  public static final String INTRAPROCEDURAL_BARRIER_CHOPPER_ALGO = "IntraproceduralBarrierChopper";
  public static final String INTRAPROCEDURAL_CHOPPER_ALGO = "IntraproceduralChopper";
  public static final String MIXED_CONTEXT_SENSITIVITY_CHOPPER_ALGO =
      "MixedContextSensitivityChopper";
  public static final String NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO = "NonSameLevelBarrierChopper";
  public static final String NON_SAME_LEVEL_CHOPPER_ALGO = "NonSameLevelChopper";
  public static final String OPT_1_CHOPPER_ALGO = "Opt1Chopper";
  public static final String REPS_ROSAY_CHOPPER_ALGO = "RepsRosayChopper";
  public static final String REPS_ROSAY_CHOPPER_UNOPT_ALGO = "RepsRosayChopperUnopt";
  public static final String SIMPLE_THREAD_BARRIER_CHOPPER_ALGO = "SimpleThreadBarrierChopper";
  public static final String SIMPLE_THREAD_CHOPPER_ALGO = "SimpleThreadChopper";
  public static final String SUMMARY_MERGED_BARRIER_CHOPPER_ALGO = "SummaryMergedBarrierChopper";
  public static final String SUMMARY_MERGED_CHOPPER_ALGO = "SummaryMergedChopper";
  public static final String THREAD_CHOPPER_ALGO = "ThreadChopper";
  public static final String TRUNCATED_NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO =
      "TruncatedNonSameLevelBarrierChopper";
  public static final String TRUNCATED_NON_SAME_LEVEL_CHOPPER_ALGO = "TruncatedNonSameLevelChopper";
  public static final String VERY_SIMPLE_THREAD_CHOPPER_ALGO = "VerySimpleThreadChopper";

  public static final List<String> ALGOS =
      new ArrayList<>(
          Arrays.asList(
              //              ALMOST_TIME_SENSITIVE_THREAD_CHOPPER_ALGO,
              CONTEXT_BASED_CHOPPER_ALGO, // 0.621 seconds
              //              CONTEXT_INSENSITIVE_CHOPPER_ALGO, // same level
              //              CONTEXT_SENSITIVE_CHOPPER_ALGO, // same level
              CONTEXT_SENSITIVE_THREAD_BARRIER_CHOPPER_ALGO, // timeout
              CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO, // timeout
              FIXED_POINT_CHOPPER_ALGO, // 16.964 seconds
              FIXED_POINT_CHOPPER_CONC_ALGO, // 15.074 seconds
              INSENSITIVE_INTERSECTION_CHOPPER_ALGO, // 5.331 seconds
              INTERSECTION_CHOPPER_ALGO, // 5.699 seconds
              INTRAPROCEDURAL_BARRIER_CHOPPER_ALGO, // timeout
              //              INTRAPROCEDURAL_CHOPPER_ALGO, // same level
              //              MIXED_CONTEXT_SENSITIVITY_CHOPPER_ALGO, // same level
              NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO, // timeout
              NON_SAME_LEVEL_CHOPPER_ALGO, // timeout
              OPT_1_CHOPPER_ALGO, // 12.547 seconds
              REPS_ROSAY_CHOPPER_ALGO, // timeout
              REPS_ROSAY_CHOPPER_UNOPT_ALGO, // UnsupportedOperationException
              SIMPLE_THREAD_BARRIER_CHOPPER_ALGO, // timeout
              SIMPLE_THREAD_CHOPPER_ALGO, // 5.381 seconds
              SUMMARY_MERGED_BARRIER_CHOPPER_ALGO, // timeout
              //              SUMMARY_MERGED_CHOPPER_ALGO, // same level
              //              THREAD_CHOPPER_ALGO,
              TRUNCATED_NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO, // timeout
              TRUNCATED_NON_SAME_LEVEL_CHOPPER_ALGO, // 5.291 seconds
              VERY_SIMPLE_THREAD_CHOPPER_ALGO // 4.82 seconds
              ));

  public static final Comparator<Lines> LINES_COMPARATOR =
      Comparator.comparingInt(Lines::getStartLineNumber).thenComparing(Lines::getEndLineNumber);

  private final String programName;
  private final SDG sdg;
  private final int sourceNode;
  private final int targetNode;
  private final String algo;
  private final Set<SDGNode> barrierNodes = new HashSet<>();
  private final Map<SDGNode, Set<SDGNode>> entryNod2ProcedureNodes;
  private final Map<String, Set<Integer>> stmtsToNotHighlight = new HashMap<>();
  private final Set<String> excludedMethods = new HashSet<>();

  public Chopper(String programName, String algo) {
    this(programName, new SDG(), -1, -1, algo, Collections.emptySet(), Collections.emptyMap());
  }

  public Chopper(
      String programName,
      SDG sdg,
      int sourceNode,
      int targetNode,
      String algo,
      Set<String> excludedMethods,
      Map<String, Set<Integer>> stmtsToNotHighlight) {
    this(
        programName,
        sdg,
        sourceNode,
        targetNode,
        algo,
        Collections.emptySet(),
        excludedMethods,
        stmtsToNotHighlight);
  }

  public Chopper(
      String programName,
      SDG sdg,
      int sourceNode,
      int targetNode,
      String algo,
      Set<SDGNode> barrierNodes,
      Set<String> excludedMethods,
      Map<String, Set<Integer>> stmtsToNotHighlight) {
    this.programName = programName;
    this.sdg = sdg;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.algo = algo;
    this.barrierNodes.addAll(barrierNodes);
    // Could be improved if we calculate nodes to procedures ahead of time
    this.entryNod2ProcedureNodes = this.sdg.sortByProcedures();
    this.excludedMethods.addAll(excludedMethods);
    this.stmtsToNotHighlight.putAll(stmtsToNotHighlight);
  }

  public static Set<SDGNode> getNodes(SDG sdg, Set<Integer> nodeIds) {
    Set<SDGNode> nodes = new HashSet<>();
    for (int i : nodeIds) {
      nodes.add(sdg.getNode(i));
    }
    return nodes;
  }

  public static Set<SDGNode> getMethodNodes(SDG sdg, Set<String> methods) {
    Set<SDGNode> nodes = new HashSet<>();
    for (String method : methods) {
      nodes.addAll(getMethodNodes(sdg, method));
    }
    return nodes;
  }

  public static Set<SDGNode> getMethodNodes(SDG sdg, String method) {
    Set<SDGNode> nodes = sdg.vertexSet();
    Set<SDGNode> methodNodes = new HashSet<>();
    for (SDGNode node : nodes) {
      if (node.getBytecodeMethod().equals(method)) {
        methodNodes.add(node);
      }
    }
    return methodNodes;
  }

  public static Map<String, SortedSet<Lines>> parseFilesToLines(Set<ChopData> chopDataSet) {
    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (ChopData chopData : chopDataSet) {
      filesToLines.put(chopData.getFileName(), new TreeSet<>(LINES_COMPARATOR));
    }
    for (ChopData chopData : chopDataSet) {
      Lines lines = new Lines(chopData.getStartLineNumber(), chopData.getEndLineNumber());
      filesToLines.get(chopData.getFileName()).add(lines);
      switch (chopData.getFileName()) {
        case "com/sleepycat/je/Database.java":
          filesToLines.get(chopData.getFileName()).add(new Lines(1335, 1335));
          break;
        case "com/sleepycat/je/Environment.java":
          filesToLines.get(chopData.getFileName()).add(new Lines(551, 551));
          filesToLines.get(chopData.getFileName()).add(new Lines(555, 555));
          break;
        case "at/favre/tools/dconvert/converters/APlatformConverter.java":
          filesToLines.get(chopData.getFileName()).add(new Lines(66, 66));
          filesToLines.get(chopData.getFileName()).add(new Lines(67, 67));
          filesToLines.get(chopData.getFileName()).add(new Lines(68, 68));
          break;
        case "com/mortennobel/imagescaling/DimensionConstrain.java":
          filesToLines.get(chopData.getFileName()).add(new Lines(18, 18));
          filesToLines.get(chopData.getFileName()).add(new Lines(27, 27));
          break;
        case "at/favre/tools/dconvert/converters/scaling/ImageHandler.java":
          filesToLines.get(chopData.getFileName()).add(new Lines(76, 76));
          filesToLines.get(chopData.getFileName()).add(new Lines(95, 95));
          filesToLines.get(chopData.getFileName()).add(new Lines(96, 96));
          filesToLines.get(chopData.getFileName()).add(new Lines(97, 97));
          filesToLines.get(chopData.getFileName()).add(new Lines(98, 98));
          filesToLines.get(chopData.getFileName()).add(new Lines(99, 99));
          filesToLines.get(chopData.getFileName()).add(new Lines(100, 100));
          filesToLines.get(chopData.getFileName()).add(new Lines(101, 101));
          break;
      }
    }
    return filesToLines;
  }

  public Set<ChopData> parseChopData(Collection<SDGNode> nodes) {
    Set<ChopData> chopDataSet = new HashSet<>();
    Set<String> files = new TreeSet<>();
    for (SDGNode node : nodes) {
      if (node == null) {
        continue;
      }
      SourceLocation sourceLocation = node.getSourceLocation();
      files.add(sourceLocation.getSourceFile());
    }

    for (String file : files) {
      System.out.println(file);
    }

    for (SDGNode node : nodes) {
      if (node == null) {
        continue;
      }
      SourceLocation sourceLocation = node.getSourceLocation();
      String file = sourceLocation.getSourceFile();
      if (file.startsWith("java/")
          || file.startsWith("javax/")
          || file.startsWith("com/ibm/wala")) {
        continue;
      }

      String method = this.getProcedure(node);
      boolean skipMethod = false;
      for (String excludedMethod : this.excludedMethods) {
        if (method.startsWith(excludedMethod)) {
          skipMethod = true;
          break;
        }
      }
      if (skipMethod) {
        continue;
      }

      boolean skipHighlight = false;
      for (Map.Entry<String, Set<Integer>> entry : this.stmtsToNotHighlight.entrySet()) {
        if (method.startsWith(entry.getKey())
            && entry.getValue().contains(sourceLocation.getStartRow())) {
          skipHighlight = true;
          break;
        }
      }
      if (skipHighlight) {
        continue;
      }

      ChopData chopData =
          new ChopData(file, sourceLocation.getStartRow(), sourceLocation.getEndRow());
      chopDataSet.add(chopData);
    }
    return chopDataSet;
  }

  public Map<String, SortedSet<Lines>> chopAndProcess() {
    Collection<SDGNode> chop = this.chop();
    Set<ChopData> chopDataSet = this.parseChopData(chop);
    return Chopper.parseFilesToLines(chopDataSet);
  }

  public Set<AbstractMap.SimpleEntry<String, String>> getProcedureConnections(
      Collection<SDGNode> chop) {
    Set<AbstractMap.SimpleEntry<String, String>> links = new HashSet<>();
    for (SDGEdge edge : this.sdg.edgeSet()) {
      if (!chop.contains(edge.getSource()) || !chop.contains(edge.getTarget())) {
        continue;
      }

      SDGNode sourceNode = edge.getSource();
      String caller = this.getProcedure(sourceNode);
      SDGNode targetNode = edge.getTarget();
      String callee = this.getProcedure(targetNode);

      boolean skipMethod = false;
      for (String excludedMethod : this.excludedMethods) {
        if (caller.startsWith(excludedMethod) || callee.startsWith(excludedMethod)) {
          skipMethod = true;
          break;
        }
      }
      if (skipMethod) {
        continue;
      }

      if (caller.contains("ResampleOp.<init>") && callee.contains("ResampleOp.<init>")) {
        continue;
      }
      if (caller.contains("APlatformConverter.convert")
          && callee.contains("LoadedImage.getImage")) {
        continue;
      }
      if (caller.contains("LoadedImage.getImage")
          && callee.contains("APlatformConverter.convert")) {
        continue;
      }

      if (!caller.equals(callee)) {
        links.add(new AbstractMap.SimpleEntry<>(caller, callee));
      }
    }
    return links;
  }

  public String getProcedure(SDGNode node) {
    for (Map.Entry<SDGNode, Set<SDGNode>> entry : this.entryNod2ProcedureNodes.entrySet()) {
      if (entry.getValue().contains(node)) {
        return entry.getKey().getLabel();
      }
    }
    throw new RuntimeException("Could not find procedure for node " + node.getId());
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
    if (ALMOST_TIME_SENSITIVE_THREAD_CHOPPER_ALGO.equals(this.algo)) {
      return new AlmostTimeSensitiveThreadChopper(this.sdg);
    }
    if (CONTEXT_BASED_CHOPPER_ALGO.equals(this.algo)) {
      return new ContextBasedChopper(this.sdg);
    }
    if (CONTEXT_INSENSITIVE_CHOPPER_ALGO.equals(this.algo)) {
      return new ContextInsensitiveChopper(this.sdg);
    }
    if (CONTEXT_SENSITIVE_CHOPPER_ALGO.equals(this.algo)) {
      return new ContextSensitiveChopper(this.sdg);
    }
    if (CONTEXT_SENSITIVE_THREAD_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      ContextSensitiveThreadBarrierChopper chopper =
          new ContextSensitiveThreadBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO.equals(this.algo)) {
      return new ContextSensitiveThreadChopper(this.sdg);
    }
    if (FIXED_POINT_CHOPPER_ALGO.equals(this.algo)) {
      return new edu.kit.joana.ifc.sdg.graph.chopper.FixedPointChopper(this.sdg);
    }
    if (FIXED_POINT_CHOPPER_CONC_ALGO.equals(this.algo)) {
      return new edu.kit.joana.ifc.sdg.graph.chopper.conc.FixedPointChopper(this.sdg);
    }
    if (INSENSITIVE_INTERSECTION_CHOPPER_ALGO.equals(this.algo)) {
      return new InsensitiveIntersectionChopper(this.sdg);
    }
    if (INTERSECTION_CHOPPER_ALGO.equals(this.algo)) {
      return new IntersectionChopper(this.sdg);
    }
    if (INTRAPROCEDURAL_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      IntraproceduralBarrierChopper chopper = new IntraproceduralBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (INTRAPROCEDURAL_CHOPPER_ALGO.equals(this.algo)) {
      return new IntraproceduralChopper(this.sdg);
    }
    if (MIXED_CONTEXT_SENSITIVITY_CHOPPER_ALGO.equals(this.algo)) {
      return new MixedContextSensitivityChopper(this.sdg);
    }
    if (NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      NonSameLevelBarrierChopper chopper = new NonSameLevelBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (NON_SAME_LEVEL_CHOPPER_ALGO.equals(this.algo)) {
      return new NonSameLevelChopper(this.sdg);
    }
    if (OPT_1_CHOPPER_ALGO.equals(this.algo)) {
      return new Opt1Chopper(this.sdg);
    }
    if (REPS_ROSAY_CHOPPER_ALGO.equals(this.algo)) {
      return new RepsRosayChopper(this.sdg);
    }
    if (REPS_ROSAY_CHOPPER_UNOPT_ALGO.equals(this.algo)) {
      return new RepsRosayChopperUnopt(this.sdg);
    }
    if (SIMPLE_THREAD_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      SimpleThreadBarrierChopper chopper = new SimpleThreadBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (SIMPLE_THREAD_CHOPPER_ALGO.equals(this.algo)) {
      return new SimpleThreadChopper(this.sdg);
    }
    if (SUMMARY_MERGED_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      SummaryMergedBarrierChopper chopper = new SummaryMergedBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (SUMMARY_MERGED_CHOPPER_ALGO.equals(this.algo)) {
      return new SummaryMergedChopper(this.sdg);
    }
    if (THREAD_CHOPPER_ALGO.equals(this.algo)) {
      return new ThreadChopper(this.sdg);
    }
    if (TRUNCATED_NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO.equals(this.algo)) {
      TruncatedNonSameLevelBarrierChopper chopper =
          new TruncatedNonSameLevelBarrierChopper(this.sdg);
      chopper.setBarrier(this.barrierNodes);
      return chopper;
    }
    if (TRUNCATED_NON_SAME_LEVEL_CHOPPER_ALGO.equals(this.algo)) {
      return new TruncatedNonSameLevelChopper(this.sdg);
    }
    if (VERY_SIMPLE_THREAD_CHOPPER_ALGO.equals(this.algo)) {
      return new VerySimpleThreadChopper(this.sdg);
    }

    throw new RuntimeException("Do not know chopping algorithm " + this.algo);
  }

  public void saveFilesToLines(Map<String, SortedSet<Lines>> filesToLines) throws IOException {
    String outputFile =
        ROOT_DIR + this.programName + "/" + this.algo + "/" + this.programName + DOT_JSON;
    File file = new File(outputFile);
    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
      throw new RuntimeException("Could not create parent dirs for " + outputFile);
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(file, filesToLines);
  }

  public Map<String, SortedSet<Lines>> readFilesToLines() throws IOException {
    File dir = new File(ROOT_DIR + this.programName + "/" + this.algo);
    Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);
    if (files.size() != 1) {
      throw new RuntimeException("Expected to get a single file at " + dir);
    }

    File filesToLinesFile = files.iterator().next();
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Set<Lines>> filesToUnsortedLines =
        mapper.readValue(filesToLinesFile, new TypeReference<Map<String, Set<Lines>>>() {});

    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (Map.Entry<String, Set<Lines>> entry : filesToUnsortedLines.entrySet()) {
      if (entry.getValue().size() == 1) {
        Lines lines = entry.getValue().iterator().next();
        if (lines.getStartLineNumber() == 0) {
          continue;
        }
      }
      filesToLines.put(entry.getKey(), new TreeSet<>(LINES_COMPARATOR));
    }
    for (Map.Entry<String, SortedSet<Lines>> entry : filesToLines.entrySet()) {
      entry.getValue().addAll(filesToUnsortedLines.get(entry.getKey()));
    }
    return filesToLines;
  }
}
