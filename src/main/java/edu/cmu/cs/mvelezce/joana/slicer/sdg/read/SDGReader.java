package edu.cmu.cs.mvelezce.joana.slicer.sdg.read;

import edu.cmu.cs.mvelezce.joana.slicer.sdg.build.SDGBuilder;
import edu.kit.joana.ifc.sdg.graph.SDG;

import java.io.IOException;

public class SDGReader {

  private final String programName;

  public SDGReader(String programName) {
    this.programName = programName;
  }

  public SDG readSDG() throws IOException {
    return SDG.readFromAndUseLessHeap("./sdgs/" + programName + SDGBuilder.DOT_PDG);
  }
}
