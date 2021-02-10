package edu.cmu.cs.mvelezce.joana.slicer.viz;

import edu.cmu.cs.mvelezce.joana.slicer.data.Lines;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HTMLGenerator {

  private final String programName;
  private final String srcDir;
  private final Map<String, SortedSet<Lines>> filesToLines;
  private final Map<String, List<String>> filesToContents = new HashMap<>();

  public HTMLGenerator(
      String programName, String srcDir, Map<String, SortedSet<Lines>> filesToLines) {
    this.programName = programName;
    this.srcDir = srcDir;
    this.filesToLines = filesToLines;
    for (String file : filesToLines.keySet()) {
      this.filesToContents.put(file, new ArrayList<>());
    }
  }

  public void readFiles() throws IOException {
    for (Map.Entry<String, List<String>> entry : this.filesToContents.entrySet()) {
      String file = entry.getKey();
      List<String> contents = entry.getValue();
      try (Stream<String> stream = Files.lines(Paths.get(srcDir + file), StandardCharsets.UTF_8)) {
        stream.forEach(contents::add);
      }
      this.filesToContents.put(file, contents);
    }
  }

  public Map<String, String> generateHTML() {
    Map<String, String> filesToHTML = new HashMap<>();
    for (String file : this.filesToContents.keySet()) {
      filesToHTML.put(file, "");
    }

    for (String file : filesToHTML.keySet()) {
      String html =
          this.generateHTMLForFile(this.filesToContents.get(file), this.filesToLines.get(file));
      filesToHTML.put(file, html);
    }
    return filesToHTML;
  }

  private String generateHTMLForFile(List<String> contents, SortedSet<Lines> sliceLines) {
    List<DomContent> body = new ArrayList<>();
    for (int i = 0; i < contents.size(); i++) {
      String line = contents.get(i);
      if (this.isLineSlice(i + 1, sliceLines)) {
        line = "* " + line;
      } else {
        line = "  " + line;
      }
      body.add(TagCreator.div(line));
    }
    return TagCreator.html(
            TagCreator.body(TagCreator.pre(TagCreator.code(body.toArray(new DomContent[0])))))
        .render();
  }

  private boolean isLineSlice(int lineNumber, SortedSet<Lines> sliceLines) {
    for (Lines lines : sliceLines) {
      if (lines.getStartLineNumber() == lineNumber) {
        return true;
      }
    }
    return false;
  }
}
