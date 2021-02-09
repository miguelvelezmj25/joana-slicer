package edu.cmu.cs.mvelezce.joana.slicer.data;

import com.google.common.base.Objects;

public class ChopData {

  private final String fileName;
  private final int startLineNumber;
  private final int endLineNumber;

  public ChopData(String fileName, int startLineNumber, int endLineNumber) {
    this.fileName = fileName;
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChopData chopData = (ChopData) o;
    return startLineNumber == chopData.startLineNumber
        && endLineNumber == chopData.endLineNumber
        && Objects.equal(fileName, chopData.fileName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(fileName, startLineNumber, endLineNumber);
  }
}
