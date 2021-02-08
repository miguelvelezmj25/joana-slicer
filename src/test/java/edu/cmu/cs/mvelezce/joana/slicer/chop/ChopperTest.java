package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ChopperTest {

  @Test
  void main() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 21;
    Chopper chopper = getChopper(programName, sourceNode, targetNode);
    chopper.chop();
  }

  private Chopper getChopper(String programName, int sourceNode, int targetNode)
      throws IOException {
    SDG sdg = getSDG(programName);
    return new Chopper(programName, sdg, sourceNode, targetNode);
  }

  private SDG getSDG(String programName) throws IOException {
    SDGReader reader = new SDGReader(programName);
    return reader.readSDG();
  }
}
