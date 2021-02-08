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
    long start = System.currentTimeMillis();
    SDG sdg = SDG.readFromAndUseLessHeap("./sdgs/" + programName + SDGBuilder.DOT_PDG);
    long end = System.currentTimeMillis();
    System.out.println("Read SDG in: " + ((end - start) / 1E3) + " seconds");
    return sdg;
  }
}
