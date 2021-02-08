package edu.cmu.cs.mvelezce.joana.slicer.sdg.analysis;

import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SDGAnalysis {

  private final SDG sdg;

  public SDGAnalysis(SDG sdg) {
    this.sdg = sdg;
  }

  public Set<SDGNode> getMethodNodes(String method) {
    Set<SDGNode> nodes = this.sdg.vertexSet();
    Set<SDGNode> methodNodes =
        new TreeSet<>(Comparator.comparingInt(n -> n.getSourceLocation().getStartRow()));
    for (SDGNode node : nodes) {
      if (node.getBytecodeMethod().equals(method)) {
        methodNodes.add(node);
      }
    }
    return methodNodes;
  }

  public Set<SDGNode> getLineNodes(int line) {
    Set<SDGNode> nodes = this.sdg.vertexSet();
    Set<SDGNode> lineNodes = new TreeSet<>(Comparator.comparingInt(SDGNode::getId));
    for (SDGNode node : nodes) {
      if (node.getSourceLocation().getStartRow() == line) {
        lineNodes.add(node);
      }
    }
    return lineNodes;
  }
}
