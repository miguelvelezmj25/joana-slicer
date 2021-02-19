package edu.cmu.cs.mvelezce.joana.slicer.sdg.build;

import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.graph.GraphIntegrity;
import edu.kit.joana.ifc.sdg.graph.SDG;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    String exclusions = "";
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
    String exclusions =
        "at\\/favre\\/tools\\/dconvert\\/converters\\/IOSConverter\n"
            + "at\\/favre\\/tools\\/dconvert\\/converters\\/WebConverter\n"
            + "at\\/favre\\/tools\\/dconvert\\/converters\\/scaling\\/NaiveGraphics2dAlgorithm\n"
            + "at\\/favre\\/tools\\/dconvert\\/converters\\/scaling\\/ProgressiveAlgorithm\n"
            + "at\\/favre\\/tools\\/dconvert\\/converters\\/WindowsConverter\n"
            + "at\\/favre\\/tools\\/dconvert\\/util\\/NinePatchScaler\n"
            + "java\\/io\\/.*\n"
            + "java\\/security\\/.*\n"
            + "java\\/util\\/concurrent\\/.*\n"
            + "java\\/util\\/HashMap\n"
            + "java\\/util\\/regex\\/.*\n";
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod, exclusions);
    SDG sdg = builder.build();
    builder.save(sdg);
  }
}
