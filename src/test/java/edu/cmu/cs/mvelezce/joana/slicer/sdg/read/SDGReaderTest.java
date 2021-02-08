package edu.cmu.cs.mvelezce.joana.slicer.sdg.read;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class SDGReaderTest {

  @Test
  void main() throws IOException {
    String programName = "main";
    SDGReader reader = new SDGReader(programName);
    System.out.println(reader.readSDG());
  }
}
