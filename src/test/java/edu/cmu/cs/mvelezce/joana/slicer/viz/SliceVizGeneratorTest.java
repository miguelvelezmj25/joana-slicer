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
    this.visualize(
        programName,
        algo,
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/",
        "edu/cmu/cs/mvelezce/features/Main.java",
        6,
        "edu/cmu/cs/mvelezce/features/Main.java",
        9);
  }

  @Test
  void ignoreMethods() throws IOException {
    String programName = "ignoreMethods";
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    this.visualize(
        programName,
        algo,
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/",
        "edu/cmu/cs/mvelezce/features/IgnoreMethods.java",
        6,
        "edu/cmu/cs/mvelezce/features/IgnoreMethods.java",
        15);
  }

  @Test
  void logger() throws IOException {
    String programName = "logger";
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    this.visualize(
        programName,
        algo,
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/",
        "edu/cmu/cs/mvelezce/features/Logger.java",
        8,
        "edu/cmu/cs/mvelezce/features/DConvert.java",
        13);
  }

  @Test
  void mainAll() {
    String programName = "main";
    for (String algo : Chopper.ALGOS) {
      System.out.println("Viz for algo: " + algo);
      try {
        this.visualize(
            programName,
            algo,
            Chopper.HOME_DIR
                + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/src/main/java/",
            "edu/cmu/cs/mvelezce/features/Main.java",
            6,
            "edu/cmu/cs/mvelezce/features/Main.java",
            9);
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
    String algo = Chopper.FIXED_POINT_CHOPPER_ALGO;
    this.visualize(
        programName,
        algo,
        Chopper.HOME_DIR
            + "/Documents/programming/java/projects/perf-debug-systems/density-converter/src/main/java/",
        "at/favre/tools/dconvert/Convert.java",
        58,
        "com/mortennobel/imagescaling/ResampleOp.java",
        321);
  }

  @Test
  void densityAll() {
    String programName = "density";
    for (String algo : Chopper.ALGOS) {
      System.out.println("Viz for algo: " + algo);
      try {
        this.visualize(
            programName,
            algo,
            Chopper.HOME_DIR
                + "/Documents/programming/java/projects/perf-debug-systems/density-converter/src/main/java/",
            "at/favre/tools/dconvert/Convert.java",
            54,
            "com/mortennobel/imagescaling/ResampleOp.java",
            284);
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

  private void visualize(
      String programName,
      String algo,
      String srcDir,
      String srcFile,
      int srcLine,
      String tgtFile,
      int tgtLine)
      throws IOException {
    Chopper chopper = new Chopper(programName, algo);
    Map<String, SortedSet<Lines>> filesToLines = chopper.readFilesToLines();
    SliceVizGenerator generator =
        new SliceVizGenerator(
            programName, srcDir, filesToLines, srcFile, srcLine, tgtFile, tgtLine, algo);
    generator.generateHTMLViz();
  }
}
