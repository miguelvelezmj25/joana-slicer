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
    SDGBuilder builder = new SDGBuilder(programName, classPath, entryMethod);
    SDG sdg = builder.build();
    builder.save(sdg);
  }
}
