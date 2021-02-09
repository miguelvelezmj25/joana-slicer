package edu.cmu.cs.mvelezce.joana.slicer.viz;

import j2html.TagCreator;
import j2html.tags.DomContent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HTMLGenerator {

  private final String programName;
  private final String srcDir;
  private final Map<File, List<String>> filesToContents = new HashMap<>();

  public HTMLGenerator(String programName, String srcDir, Set<String> filesToRead) {
    this.programName = programName;
    this.srcDir = srcDir;
    for (String file : filesToRead) {
      this.filesToContents.put(new File(file), new ArrayList<>());
    }
  }

  public void readFiles() throws IOException {
    for (Map.Entry<File, List<String>> entry : this.filesToContents.entrySet()) {
      File file = entry.getKey();
      List<String> contents = entry.getValue();
      try (Stream<String> stream =
          Files.lines(Paths.get(srcDir + file.getPath()), StandardCharsets.UTF_8)) {
        stream.forEach(contents::add);
      }
      this.filesToContents.put(file, contents);
    }
  }

  public Map<File, String> generateHTML() {
    Map<File, String> filesToHTML = new HashMap<>();
    for (File file : this.filesToContents.keySet()) {
      filesToHTML.put(file, "");
    }

    for (Map.Entry<File, List<String>> entry : this.filesToContents.entrySet()) {
      List<DomContent> body = new ArrayList<>();
      for (String line : entry.getValue()) {
        if (line.isEmpty()) {
          line = " ";
        }
        body.add(TagCreator.div(line));
      }
      String html =
          TagCreator.html(
                  TagCreator.body(TagCreator.pre(TagCreator.code(body.toArray(new DomContent[0])))))
              .render();
      filesToHTML.put(entry.getKey(), html);
    }
    return filesToHTML;
  }
}
