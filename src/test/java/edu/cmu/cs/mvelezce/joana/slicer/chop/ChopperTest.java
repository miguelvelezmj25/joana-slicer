package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
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
  void density() throws IOException {
    String programName = "density";
    int sourceNode = 7;
    int targetNode = 33698;
    String algo = Chopper.SIMPLE_THREAD_BARRIER_CHOPPER_ALGO;
    Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
    chopper.saveFilesToLines(filesToLines);
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
