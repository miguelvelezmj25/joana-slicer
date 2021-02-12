package edu.cmu.cs.mvelezce.joana.slicer.chop;

import com.mijecu25.meme.utils.execute.Executor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ChopperEvalTest {

  @Test
  void main() throws IOException, InterruptedException {
    String programName = "main";
    int sourceNode = 5;
    int targetNode = 22;
    this.eval(programName, sourceNode, targetNode);
  }

  @Test
  void density() throws IOException, InterruptedException {
    String programName = "density";
    int sourceNode = 7;
    int targetNode = 33698;
    this.eval(programName, sourceNode, targetNode);
  }

  private void eval(String programName, int sourceNode, int targetNode)
      throws InterruptedException, IOException {
    ProcessBuilder builder = new ProcessBuilder();
    for (String algo : Chopper.ALGOS) {
      List<String> command = this.buildCommand(programName, sourceNode, targetNode, algo);
      builder.command(command);

      Process process = builder.start();
      if (process.waitFor(10, TimeUnit.MINUTES)) {
        System.out.println("Algo: " + algo + " finished");
        Executor.processOutput(process);
        try {
          Executor.processError(process);
        } catch (RuntimeException re) {
          re.printStackTrace();
        }
      } else {
        System.err.println("Algo: " + algo + " timeout");
      }

      process = process.destroyForcibly();
      process.waitFor();

      System.gc();
      System.out.println();
      System.out.println();
      System.out.println("=============================");
      System.out.println();
      System.out.println();
      System.gc();
      Thread.sleep(20000);
    }
  }

  private List<String> buildCommand(
      String programName, int sourceNode, int targetNode, String algo) {
    List<String> command = new ArrayList<>();
    command.add("java");
    command.add("-Xmx26g");
    command.add("-cp");
    command.add(
        "./target/classes"
            + ":../joana/dist/joana.api.jar"
            + ":/Users/miguelvelez/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.12.0/jackson-core-2.12.0.jar"
            + ":/Users/miguelvelez/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.12.0/jackson-annotations-2.12.0.jar"
            + ":/Users/miguelvelez/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.12.0/jackson-databind-2.12.0.jar");
    command.add("edu.cmu.cs.mvelezce.joana.slicer.chop.ChopperEval");
    command.add(programName);
    command.add(String.valueOf(sourceNode));
    command.add(String.valueOf(targetNode));
    command.add(algo);
    return command;
  }
}
