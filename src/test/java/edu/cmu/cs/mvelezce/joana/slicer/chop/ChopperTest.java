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

  @Test
  void main() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 22;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
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
    int sourceNode = 7;
    int targetNode = 33698;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
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
