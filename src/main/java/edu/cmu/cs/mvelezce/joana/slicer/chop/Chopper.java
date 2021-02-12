package edu.cmu.cs.mvelezce.joana.slicer.chop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.cmu.cs.mvelezce.joana.slicer.data.ChopData;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.kit.joana.ifc.sdg.graph.SDG;
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
              ALMOST_TIME_SENSITIVE_THREAD_CHOPPER_ALGO,
              CONTEXT_BASED_CHOPPER_ALGO,
              CONTEXT_INSENSITIVE_CHOPPER_ALGO,
              CONTEXT_SENSITIVE_CHOPPER_ALGO,
              CONTEXT_SENSITIVE_THREAD_BARRIER_CHOPPER_ALGO,
              CONTEXT_SENSITIVE_THREAD_CHOPPER_ALGO,
              FIXED_POINT_CHOPPER_ALGO,
              FIXED_POINT_CHOPPER_CONC_ALGO,
              INSENSITIVE_INTERSECTION_CHOPPER_ALGO,
              INTERSECTION_CHOPPER_ALGO,
              INTRAPROCEDURAL_BARRIER_CHOPPER_ALGO,
              INTRAPROCEDURAL_CHOPPER_ALGO,
              MIXED_CONTEXT_SENSITIVITY_CHOPPER_ALGO,
              NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO,
              NON_SAME_LEVEL_CHOPPER_ALGO,
              OPT_1_CHOPPER_ALGO,
              REPS_ROSAY_CHOPPER_ALGO,
              REPS_ROSAY_CHOPPER_UNOPT_ALGO,
              SIMPLE_THREAD_BARRIER_CHOPPER_ALGO,
              SIMPLE_THREAD_CHOPPER_ALGO,
              SUMMARY_MERGED_BARRIER_CHOPPER_ALGO,
              SUMMARY_MERGED_CHOPPER_ALGO,
              THREAD_CHOPPER_ALGO,
              TRUNCATED_NON_SAME_LEVEL_BARRIER_CHOPPER_ALGO,
              TRUNCATED_NON_SAME_LEVEL_CHOPPER_ALGO,
              VERY_SIMPLE_THREAD_CHOPPER_ALGO));

  private static final Comparator<Lines> LINES_COMPARATOR =
      Comparator.comparingInt(Lines::getStartLineNumber).thenComparing(Lines::getEndLineNumber);

  private final String programName;
  private final SDG sdg;
  private final int sourceNode;
  private final int targetNode;
  private final String algo;
  private final Set<SDGNode> barrierNodes = new HashSet<>();

  public Chopper(String programName) {
    this(programName, new SDG(), -1, -1, "");
  }

  public Chopper(String programName, SDG sdg, int sourceNode, int targetNode, String algo) {
    this(programName, sdg, sourceNode, targetNode, algo, Collections.emptySet());
  }

  public Chopper(
      String programName,
      SDG sdg,
      int sourceNode,
      int targetNode,
      String algo,
      Set<SDGNode> barrierNodes) {
    this.programName = programName;
    this.sdg = sdg;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.algo = algo;
    this.barrierNodes.addAll(barrierNodes);
  }

  public static Set<ChopData> parseChopData(Collection<SDGNode> nodes) {
    Set<ChopData> chopDataSet = new HashSet<>();
    for (SDGNode node : nodes) {
      SourceLocation sourceLocation = node.getSourceLocation();
      String file = sourceLocation.getSourceFile();
      if (file.startsWith("java/")
          || file.startsWith("javax/")
          || file.startsWith("com/ibm/wala")) {
        continue;
      }
      ChopData chopData =
          new ChopData(file, sourceLocation.getStartRow(), sourceLocation.getEndRow());
      chopDataSet.add(chopData);
    }
    return chopDataSet;
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
