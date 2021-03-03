package edu.cmu.cs.mvelezce.joana.slicer.sdg.build;

import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.graph.GraphIntegrity;
import edu.cmu.cs.mvelezce.joana.slicer.coverage.CoverageReportParser;
import edu.kit.joana.ifc.sdg.graph.SDG;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

class SDGBuilderTest {

  private static final String USER_HOME = System.getProperty("user.home");

  @Test
  void main()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "main";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Main.main([Ljava/lang/String;)V";
    String exclusions =
        "java\\/io\\/.*\n"
            + "java\\/security\\/.*\n"
            + "java\\/util\\/concurrent\\/.*\n"
            + "java\\/util\\/HashMap\n"
            + "java\\/util\\/regex\\/.*\n";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void ignoreImplementations()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "ignoreImplementations";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod =
        "edu.cmu.cs.mvelezce.features.IgnoreImplementations.main([Ljava/lang/String;)V";
    String exclusions = "edu\\/cmu\\/cs\\/mvelezce\\/features\\/Cat\n";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void ignoreMethods()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "ignoreMethods";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.IgnoreMethods.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void loop()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "loop";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Loop.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void implicit()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "implicit";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Implicit.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void logger()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "logger";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Logger.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void generics()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "generics";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Generics.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void dimension()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "dimension";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/performance-mapper-evaluation/phosphor/tracing/target/classes";
    String entryMethod = "edu.cmu.cs.mvelezce.features.Dimension.main([Ljava/lang/String;)V";
    String exclusions = "";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  @Test
  void density()
      throws IOException, CancelException, ClassHierarchyException,
          GraphIntegrity.UnsoundGraphException {
    String programName = "density";
    String classPath =
        USER_HOME
            + "/Documents/programming/java/projects/perf-debug-systems/density-converter/target/classes";
    String entryMethod = "at.favre.tools.dconvert.Convert.main([Ljava/lang/String;)V";
    String exclusions = this.getExclusions(programName, 6);
    exclusions +=
        "at\\/favre\\/tools\\/dconvert\\/converters\\/scaling\\/ProgressiveAlgorithm\n"
            + "java\\/io\\/.*\n"
            + "java\\/security\\/.*\n"
            + "java\\/util\\/concurrent\\/.*\n"
            + "java\\/util\\/HashMap\n"
            + "java\\/util\\/regex\\/.*\n";
    exclusions =
        exclusions.replace(
            "at\\/favre\\/tools\\/dconvert\\/converters\\/descriptors\\/AndroidDensityDescriptor\n",
            "");
    exclusions =
        exclusions.replace(
            "at\\/favre\\/tools\\/dconvert\\/converters\\/descriptors\\/DensityDescriptor\n", "");
    exclusions = exclusions.replace("com\\/mortennobel\\/imagescaling\\/DimensionConstrain\n", "");
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }

  private String getExclusions(String programName, int coverageThreshold) throws IOException {
    CoverageReportParser parser = new CoverageReportParser(programName);
    Map<String, Integer> classes2Coverages = parser.read();

    StringBuilder exclusions = new StringBuilder();
    for (Map.Entry<String, Integer> entry : classes2Coverages.entrySet()) {
      if (entry.getValue() > coverageThreshold) {
        continue;
      }
      String className = entry.getKey();
      className = className.replace(".", "\\/");
      exclusions.append(className).append("\n");
    }
    return exclusions.toString();
  }
}
