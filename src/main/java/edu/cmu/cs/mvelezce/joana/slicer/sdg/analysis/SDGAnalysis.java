package edu.cmu.cs.mvelezce.joana.slicer.sdg.analysis;

import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
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

  public SortedSet<String> getUnresolvedCallTargets() {
    SortedSet<String> unresolvedCallTargets = new TreeSet<>(String::compareTo);
    Set<SDGNode> nodes = this.sdg.vertexSet();
    for (SDGNode node : nodes) {
      String unresolvedCallTarget = node.getUnresolvedCallTarget();
      if (unresolvedCallTarget == null
          || unresolvedCallTarget.startsWith("java")
          || unresolvedCallTarget.startsWith("sun")
          || unresolvedCallTarget.startsWith("com.sun")
          || unresolvedCallTarget.startsWith("com.twelvemonkeys")
          || unresolvedCallTarget.startsWith("com.ibm.wala")) {
        continue;
      }
      unresolvedCallTargets.add(unresolvedCallTarget);
    }
    return unresolvedCallTargets;
  }

  public Set<String> getAnalyzedMethods() {
    Set<SDGNode> nodes = this.sdg.vertexSet();
    Set<String> methods = new TreeSet<>(String::compareTo);
    for (SDGNode node : nodes) {
      if (!node.getBytecodeMethod().startsWith("at.")
          && !node.getBytecodeMethod().startsWith("com.mortennobel.")) {
        continue;
      }
      methods.add(node.getBytecodeMethod());
    }
    return methods;
  }
}
