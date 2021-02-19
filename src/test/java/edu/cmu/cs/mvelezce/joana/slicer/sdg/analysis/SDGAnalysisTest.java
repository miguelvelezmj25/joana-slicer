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

    analysis.getUnresolvedCallTargets();
    System.out.println();
  }

  @Test
  void loop() throws IOException {
    String programName = "loop";
    SDGAnalysis analysis = getSDGAnalysis(programName);

    int line = 26;
    Set<SDGNode> lineNodes = analysis.getLineNodes(line);

    analysis.getUnresolvedCallTargets();
    System.out.println();
  }

  @Test
  void density() throws IOException {
    String programName = "density";
    SDGAnalysis analysis = getSDGAnalysis(programName);

    String method = "at.favre.tools.dconvert.Convert.main([Ljava/lang/String;)V";
    Set<SDGNode> srcMethodNodes = analysis.getMethodNodes(method);

    method = "at.favre.tools.dconvert.DConvert.execute(Lat/favre/tools/dconvert/arg/Arguments;ZLat/favre/tools/dconvert/DConvert$HandlerCallback;)V";
    Set<SDGNode> tgtMethodNodes = analysis.getMethodNodes(method);

    Set<String> analyzedMethods = analysis.getAnalyzedMethods();
    System.out.println("analyzed methods");
    for (String analyzedMethod : analyzedMethods) {
      System.out.println(analyzedMethod);
    }
    System.out.println();

    Set<String> unresolvedCallTargets = analysis.getUnresolvedCallTargets();
    System.out.println("Unresolved targets");
    for (String unresolvedTarget : unresolvedCallTargets) {
      System.out.println(unresolvedTarget);
    }
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
