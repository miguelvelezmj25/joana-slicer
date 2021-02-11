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
    Chopper chopper = new Chopper(programName);
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
            9);
    generator.generateHTMLViz();
  }

  @Test
  void density() throws IOException {
    String programName = "density";
    Chopper chopper = new Chopper(programName);
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
            284);
    generator.generateHTMLViz();
  }
}
