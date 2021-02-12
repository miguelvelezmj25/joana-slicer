package edu.cmu.cs.mvelezce.joana.slicer.viz;

import edu.cmu.cs.mvelezce.joana.slicer.chop.Chopper;
import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

class SliceVizGeneratorTest {

  @Test
  void main() throws IOException {
    String programName = "main";
    String algo = Chopper.SIMPLE_THREAD_CHOPPER_ALGO;
    Chopper chopper = new Chopper(programName, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.readFilesToLines();
    String srcDir =
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/";
    SliceVizGenerator generator =
        new SliceVizGenerator(
            programName,
            srcDir,
            filesToLines,
            "edu/cmu/cs/mvelezce/features/Main.java",
            6,
            "edu/cmu/cs/mvelezce/features/Main.java",
            9,
            algo);
    generator.generateHTMLViz();
  }

  @Test
  void mainAll() throws IOException {
    String programName = "main";
    for (String algo : Chopper.ALGOS) {
      System.out.println("Viz for algo: " + algo);
      try {
        Chopper chopper = new Chopper(programName, algo);
        Map<String, SortedSet<Lines>> filesToLines = chopper.readFilesToLines();
        String srcDir =
            Chopper.HOME_DIR
                + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/";
        SliceVizGenerator generator =
            new SliceVizGenerator(
                programName,
                srcDir,
                filesToLines,
                "edu/cmu/cs/mvelezce/features/Main.java",
                6,
                "edu/cmu/cs/mvelezce/features/Main.java",
                9,
                algo);
        generator.generateHTMLViz();
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println();
      System.out.println();
      System.out.println("===============================================");
      System.out.println();
      System.out.println();
    }
  }

  @Test
  void density() throws IOException {
    String programName = "density";
    String algo = Chopper.SIMPLE_THREAD_CHOPPER_ALGO;
    Chopper chopper = new Chopper(programName, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.readFilesToLines();
    String srcDir =
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/perf-debug-systems/density-converter/src/main/java/";
    SliceVizGenerator generator =
        new SliceVizGenerator(
            programName,
            srcDir,
            filesToLines,
            "at/favre/tools/dconvert/Convert.java",
            54,
            "com/mortennobel/imagescaling/ResampleOp.java",
            284,
            algo);
    generator.generateHTMLViz();
  }
}
