package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

class ChopperTest {
  // -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005

  @Test
  void loop() throws IOException {
    String programName = "loop";
    int sourceNode = 5;
    int targetNode = 104;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void implicit() throws IOException {
    String programName = "implicit";
    int sourceNode = 6;
    int targetNode = 94;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void logger() throws IOException {
    String programName = "logger";
    int sourceNode = 6;
    int targetNode = 168;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void dimension() throws IOException {
    String programName = "dimension";
    int sourceNode = 6;
    int targetNode = 89;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void main() throws IOException {
    String programName = "main";
    int sourceNode = 6;
    int targetNode = 22;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void ignoreImplementations() throws IOException {
    String programName = "ignoreImplementations";
    int sourceNode = 6;
    int targetNode = 29;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void ignoreMethods() throws IOException {
    String programName = "ignoreMethods";
    int sourceNode = 7;
    int targetNode = 32;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void mainBarrierIds() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 22;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Set<Integer> nodeIds = new HashSet<>();
    nodeIds.add(72);
    nodeIds.add(76);
    Chopper chopper = getChopperNodesBarrier(programName, sourceNode, targetNode, algo, nodeIds);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void mainBarrierMethods() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 22;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Set<String> methods = new HashSet<>();
    methods.add("edu.cmu.cs.mvelezce.features.Main.getInt(I)I");
    Chopper chopper = getChopperMethodsBarrier(programName, sourceNode, targetNode, algo, methods);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void mainBarrierNodes() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 22;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Set<Integer> nodeIds = new HashSet<>();
    nodeIds.add(72);
    nodeIds.add(76);
    Chopper chopper = getChopperNodesBarrier(programName, sourceNode, targetNode, algo, nodeIds);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void density() throws IOException {
    String programName = "density";
    int sourceNode = 12;
    //        int targetNode = 6636;
    //    int targetNode = 12879;
    int targetNode = 9374;
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  @Test
  void densityBarrierMethods() throws IOException {
    String programName = "density";
    int sourceNode = 7;
    int targetNode = 33698;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Set<String> methods = new HashSet<>();
    methods.add("at.favre.tools.dconvert.Convert.getPlatform(Ljava/lang/String;)Ljava/util/Set");
    Chopper chopper = getChopperMethodsBarrier(programName, sourceNode, targetNode, algo, methods);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
  }

  private Chopper getChopperMethodsBarrier(
      String programName, int sourceNode, int targetNode, String algo, Set<String> methods)
      throws IOException {
    SDG sdg = getSDG(programName);
    Set<SDGNode> barrierNodes = Chopper.getMethodNodes(sdg, methods);
    return new Chopper(programName, sdg, sourceNode, targetNode, algo, barrierNodes);
  }

  private Chopper getChopperNodesBarrier(
      String programName, int sourceNode, int targetNode, String algo, Set<Integer> nodeIds)
      throws IOException {
    SDG sdg = getSDG(programName);
    Set<SDGNode> barrierNodes = Chopper.getNodes(sdg, nodeIds);
    return new Chopper(programName, sdg, sourceNode, targetNode, algo, barrierNodes);
  }

  private Chopper getChopper(String programName, int sourceNode, int targetNode, String algo)
      throws IOException {
    SDG sdg = getSDG(programName);
    return new Chopper(programName, sdg, sourceNode, targetNode, algo);
  }

  private SDG getSDG(String programName) throws IOException {
    SDGReader reader = new SDGReader(programName);
    return reader.readSDG();
  }
}
