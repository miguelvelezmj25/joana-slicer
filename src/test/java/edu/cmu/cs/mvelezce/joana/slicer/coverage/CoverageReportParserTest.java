package edu.cmu.cs.mvelezce.joana.slicer.coverage;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class CoverageReportParserTest {

  @Test
  void density() throws IOException {
    String programName = "density";
    CoverageReportParser parser = new CoverageReportParser(programName);
    parser.parse();
  }
}
