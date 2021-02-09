package edu.cmu.cs.mvelezce.joana.slicer.data;

import com.google.common.base.Objects;

public class Lines {

  private final int startLineNumber;
  private final int endLineNumber;

  // Dummy constructor for fasterxml
  private Lines() {
    this.startLineNumber = -1;
    this.endLineNumber = -1;
  }

  public Lines(int startLineNumber, int endLineNumber) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lines lines = (Lines) o;
    return startLineNumber == lines.startLineNumber && endLineNumber == lines.endLineNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(startLineNumber, endLineNumber);
  }
}
