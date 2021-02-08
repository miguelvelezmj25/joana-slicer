package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.chopper.conc.ContextSensitiveThreadChopper;

import java.util.Collection;

public class Chopper {

  private final String programName;
  private final SDG sdg;
  private final int sourceNode;
  private final int targetNode;

  public Chopper(String programName, SDG sdg, int sourceNode, int targetNode) {
    this.programName = programName;
    this.sdg = sdg;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
  }

  public Collection<SDGNode> chop() {
    long start = System.currentTimeMillis();
    edu.kit.joana.ifc.sdg.graph.chopper.Chopper chopper =
        new ContextSensitiveThreadChopper(this.sdg);
    Collection<SDGNode> chop =
        chopper.chop(this.sdg.getNode(this.sourceNode), this.sdg.getNode(this.targetNode));
    long end = System.currentTimeMillis();
    System.out.println("Chopper in: " + ((end - start) / 1E3) + " seconds");
    return chop;
  }
}
