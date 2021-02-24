package edu.cmu.cs.mvelezce.joana.slicer.coverage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CoverageReportParser {
  private static final Pattern LINE_COVERAGE_PATTERN =
      Pattern.compile("<span class=\"absValue\"> \\((.*?)/(.*?)\\) </span>");
  private static final Pattern TITLE_PATTERN =
      Pattern.compile("<title>Coverage Report :: (.*?)</title>");
  private static final String PARSED_COVERAGE_ROOT =
      "./src/main/resources/coverage/parsed/programs/";
  private static final String COVERAGE_REPORT_ROOT =
      "./src/main/resources/coverage/report/programs/";
  private static final String DOT_JSON = ".json";

  private final String programName;

  public CoverageReportParser(String programName) {
    this.programName = programName;
  }

  public void parse() throws IOException {
    List<File> indexFiles = this.getIndexFiles();
    Map<String, Integer> fullyQualifiedClasses2LineCoverages = new HashMap<>();
    for (File file : indexFiles) {
      String html = this.getHtml(file.getAbsolutePath());
      Document document = Jsoup.parse(html);
      List<String> classes = this.getClasses(document);
      Map<String, Integer> classes2LineCoverages = this.getClasses2LineCoverages(document, classes);
      String packageName = this.getPackage(document);
      fullyQualifiedClasses2LineCoverages.putAll(
          this.getFullyQualifiedClasses2LineCoverages(packageName, classes2LineCoverages));
    }
    this.save(fullyQualifiedClasses2LineCoverages);
  }

  private void save(Map<String, Integer> fullyQualifiedClasses2LineCoverages) throws IOException {
    List<Map.Entry<String, Integer>> list =
        new ArrayList<>(fullyQualifiedClasses2LineCoverages.entrySet());
    list.sort(Map.Entry.comparingByValue());
    Collections.reverse(list);

    Map<String, Integer> sortedResult = new LinkedHashMap<>();
    for (Map.Entry<String, Integer> entry : list) {
      sortedResult.put(entry.getKey(), entry.getValue());
    }

    String outputFile = PARSED_COVERAGE_ROOT + this.programName + "/" + this.programName + DOT_JSON;
    File file = new File(outputFile);
    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
      throw new RuntimeException("Could not create parent dirs for " + outputFile);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writeValue(file, sortedResult);
  }

  private List<File> getIndexFiles() {
    Collection<File> files =
        FileUtils.listFilesAndDirs(
            new File(COVERAGE_REPORT_ROOT + this.programName),
            TrueFileFilter.INSTANCE,
            DirectoryFileFilter.DIRECTORY);
    List<File> indexFiles = new ArrayList<>();
    for (File file : files) {
      if (!file.isFile()
          || !file.getName().equals("index.html")
          || file.getParentFile().getParent().equals(COVERAGE_REPORT_ROOT + this.programName)) {
        continue;
      }
      indexFiles.add(file);
    }
    return indexFiles;
  }

  private Map<String, Integer> getFullyQualifiedClasses2LineCoverages(
      String packageName, Map<String, Integer> classes2LineCoverages) {
    Map<String, Integer> fullyQualifiedClasses2LineCoverages = new HashMap<>();
    for (Map.Entry<String, Integer> entry : classes2LineCoverages.entrySet()) {
      fullyQualifiedClasses2LineCoverages.put(packageName + "." + entry.getKey(), entry.getValue());
    }
    return fullyQualifiedClasses2LineCoverages;
  }

  private Map<String, Integer> getClasses2LineCoverages(Document document, List<String> classes) {
    Map<String, Integer> classes2LineCoverages = new HashMap<>();
    for (String className : classes) {
      classes2LineCoverages.put(className, 0);
    }

    Element table = document.getElementsByClass("coverageStats").get(1);
    Elements rows = table.getElementsByTag("tr");
    for (int i = 1; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements columns = row.getElementsByTag("td");
      Element lineCoverageElement = columns.get(columns.size() - 1);
      String lineCoverage = lineCoverageElement.getElementsByClass("absValue").get(0).toString();
      Matcher matcher = LINE_COVERAGE_PATTERN.matcher(lineCoverage);
      if (!matcher.find()) {
        throw new RuntimeException("Could not find the package in the title tag");
      }
      int linesCovered = Integer.parseInt(matcher.group(1).trim());

      String className =
          row.getElementsByClass("name")
              .get(0)
              .childNodes()
              .get(0)
              .childNodes()
              .get(0)
              .toString()
              .trim();
      if (!classes2LineCoverages.containsKey(className)) {
        throw new RuntimeException("Found class name that was not considered before");
      }
      classes2LineCoverages.put(className, linesCovered);
    }
    return classes2LineCoverages;
  }

  private List<String> getClasses(Document document) {
    Element table = document.getElementsByClass("coverageStats").get(1);
    Elements classEntries = table.getElementsByClass("name");
    List<String> classes = new ArrayList<>();
    for (int i = 1; i < classEntries.size(); i++) {
      Element classEntry = classEntries.get(i);
      String className =
          classEntry.getElementsByTag("a").get(0).childNodes().get(0).toString().trim();
      classes.add(className);
    }
    return classes;
  }

  private String getHtml(String fileName) throws IOException {
    StringBuilder htmlBuilder = new StringBuilder();
    try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
      stream.forEach(s -> htmlBuilder.append(s).append("\n"));
    }
    return htmlBuilder.toString();
  }

  private String getPackage(Document document) {
    String title = document.getElementsByTag("title").toString().trim();
    Matcher matcher = TITLE_PATTERN.matcher(title);
    if (matcher.find()) {
      return matcher.group(1);
    }
    throw new RuntimeException("Could not find the package in the title tag");
  }

  public Map<String, Integer> read() throws IOException {
    File file =
        new File(PARSED_COVERAGE_ROOT + this.programName + "/" + this.programName + DOT_JSON);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, new TypeReference<Map<String, Integer>>() {});
  }
}
