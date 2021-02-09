package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.data.ChopData;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import edu.kit.joana.ifc.sdg.graph.chopper.conc.ContextSensitiveThreadChopper;
import edu.kit.joana.util.SourceLocation;

import java.util.*;

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

  public static Set<ChopData> parseChopData(Collection<SDGNode> nodes) {
    Set<ChopData> chopDataSet = new HashSet<>();
    for (SDGNode node : nodes) {
      SourceLocation sourceLocation = node.getSourceLocation();
      ChopData chopData =
          new ChopData(
              sourceLocation.getSourceFile(),
              sourceLocation.getStartRow(),
              sourceLocation.getEndRow());
      chopDataSet.add(chopData);
    }
    return chopDataSet;
  }

  public static Map<String, SortedSet<Lines>> parseFilesToLines(Set<ChopData> chopDataSet) {
    Map<String, SortedSet<Lines>> filesToLines = new HashMap<>();
    for (ChopData chopData : chopDataSet) {
      filesToLines.put(
          chopData.getFileName(),
          new TreeSet<>(
              Comparator.comparingInt(Lines::getStartLineNumber)
                  .thenComparing(Lines::getEndLineNumber)));
    }
    for (ChopData chopData : chopDataSet) {
      Lines lines = new Lines(chopData.getStartLineNumber(), chopData.getEndLineNumber());
      filesToLines.get(chopData.getFileName()).add(lines);
    }
    return filesToLines;
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
