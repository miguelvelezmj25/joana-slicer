package edu.cmu.cs.mvelezce.joana.slicer.viz;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class HTMLGenerator {

  private final String programName;
  private final String srcDir;
  private final Map<File, String> filesToContents = new HashMap<>();

  public HTMLGenerator(String programName, String srcDir, Set<String> filesToRead) {
    this.programName = programName;
    this.srcDir = srcDir;
    for (String file : filesToRead) {
      this.filesToContents.put(new File(file), "");
    }
  }

  public void readFiles() throws IOException {
    for (Map.Entry<File, String> entry : this.filesToContents.entrySet()) {
      File file = entry.getKey();
      StringBuilder contents = new StringBuilder();
      try (Stream<String> stream =
          Files.lines(Paths.get(srcDir + file.getPath()), StandardCharsets.UTF_8)) {
        stream.forEach(s -> contents.append(s).append("\n"));
      }
      this.filesToContents.put(file, contents.toString());
    }
  }
}
