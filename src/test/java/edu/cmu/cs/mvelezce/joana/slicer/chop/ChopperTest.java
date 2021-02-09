package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.data.ChopData;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

class ChopperTest {

  @Test
  void main() throws IOException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 21;
    Chopper chopper = getChopper(programName, sourceNode, targetNode);
    Collection<SDGNode> chop = chopper.chop();
    Set<ChopData> chopDataSet = Chopper.parseChopData(chop);
    Map<String, SortedSet<Lines>> filesToLines = Chopper.parseFilesToLines(chopDataSet);
    chopper.saveFilesToLines(filesToLines);
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
