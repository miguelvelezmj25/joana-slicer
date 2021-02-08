package edu.cmu.cs.mvelezce.joana.slicer.sdg.analysis;

import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

class SDGAnalysisTest {

  @Test
  void main() throws IOException {
    String programName = "main";
    SDGAnalysis analysis = getSDGAnalysis(programName);

    String method = "edu.cmu.cs.mvelezce.features.Main.main([Ljava/lang/String;)V";
    Set<SDGNode> methodNodes = analysis.getMethodNodes(method);

    int line = 9;
    Set<SDGNode> lineNodes = analysis.getLineNodes(line);
    System.out.println();
  }

  private SDGAnalysis getSDGAnalysis(String programName) throws IOException {
    SDG sdg = getSDG(programName);
    return new SDGAnalysis(sdg);
  }

  private SDG getSDG(String programName) throws IOException {
    SDGReader reader = new SDGReader(programName);
    return reader.readSDG();
  }
}
