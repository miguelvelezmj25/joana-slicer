package edu.cmu.cs.mvelezce.joana.slicer.chop;

import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

public class ChopperEval {

  public static void main(String[] args) {
    try {
      String programName = args[0];
      int sourceNode = Integer.parseInt(args[1]);
      int targetNode = Integer.parseInt(args[2]);
      String algo = args[3];
      Chopper chopper = getChopper(programName, sourceNode, targetNode, algo);
      Map<String, SortedSet<Lines>> filesToLines = chopper.chopAndProcess();
      chopper.saveFilesToLines(filesToLines);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Chopper getChopper(String programName, int sourceNode, int targetNode, String algo)
      throws IOException {
    SDG sdg = getSDG(programName);
    return new Chopper(programName, sdg, sourceNode, targetNode, algo);
  }

  private static SDG getSDG(String programName) throws IOException {
    SDGReader reader = new SDGReader(programName);
    return reader.readSDG();
  }
}
