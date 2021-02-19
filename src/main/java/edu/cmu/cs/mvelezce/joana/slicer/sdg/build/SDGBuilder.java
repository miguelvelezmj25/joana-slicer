package edu.cmu.cs.mvelezce.joana.slicer.sdg.build;

import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.graph.GraphIntegrity;
import edu.kit.joana.api.sdg.SDGConfig;
import edu.kit.joana.api.sdg.SDGProgram;
import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.SDGSerializer;
import edu.kit.joana.ifc.sdg.mhpoptimization.MHPType;
import edu.kit.joana.ui.annotations.PruningPolicy;
import edu.kit.joana.util.Stubs;
import edu.kit.joana.wala.core.SDGBuilder.ExceptionAnalysis;
import edu.kit.joana.wala.core.SDGBuilder.FieldPropagation;
import edu.kit.joana.wala.core.SDGBuilder.PointsToPrecision;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class SDGBuilder {

  public static final String ROOT_DIR = "./src/main/resources/sdgs/";
  public static final String DOT_PDG = ".pdg";

  private final String programName;
  private final SDGConfig sdgConfig;

  public SDGBuilder(String programName, String classPath, String entryMethod, String exclusions) {
    this.programName = programName;
    this.sdgConfig = new SDGConfig(classPath, entryMethod, Stubs.JRE_17);

    this.sdgConfig.setComputeInterferences(false);
    this.sdgConfig.setExceptionAnalysis(ExceptionAnalysis.IGNORE_ALL);
    this.sdgConfig.setExclusions(
        edu.kit.joana.wala.core.SDGBuilder.STD_EXCLUSION_REG_EXP + exclusions);
    this.sdgConfig.setFieldPropagation(FieldPropagation.OBJ_GRAPH);
    this.sdgConfig.setMhpType(MHPType.NONE);
    this.sdgConfig.setPointsToPrecision(PointsToPrecision.INSTANCE_BASED);
    this.sdgConfig.setPruningPolicy(PruningPolicy.APPLICATION);

    //        this.sdgConfig.setControlDependenceVariant(ControlDependenceVariant.CLASSIC);
    //        this.sdgConfig.setIgnoreIndirectFlows(true);

    System.out.println("CGConsumer: " + this.sdgConfig.getCGConsumer());
    System.out.println("ClassPath: " + this.sdgConfig.getClassPath());
    System.out.println(
        "ClasspathAddEntriesFromMANIFEST: " + this.sdgConfig.getClasspathAddEntriesFromMANIFEST());
    System.out.println("ContextSelector: " + this.sdgConfig.getContextSelector());
    System.out.println("ControlDependenceVariant: " + this.sdgConfig.getControlDependenceVariant());
    System.out.println(
        "DefaultExceptionMethodState: " + this.sdgConfig.getDefaultExceptionMethodState());
    System.out.println("DynamicDispatchHandling: " + this.sdgConfig.getDynamicDispatchHandling());
    System.out.println("EntryMethod: " + this.sdgConfig.getEntryMethod());
    System.out.println("ExceptionAnalysis: " + this.sdgConfig.getExceptionAnalysis());
    System.out.println("Exclusions: " + this.sdgConfig.getExclusions());
    System.out.println("FieldHelperOptions: " + this.sdgConfig.getFieldHelperOptions().getName());
    System.out.println("FieldPropagation: " + this.sdgConfig.getFieldPropagation());
    System.out.println("IgnoreIndirectFlows: " + this.sdgConfig.getIgnoreIndirectFlows());
    System.out.println("MethodFilter: " + this.sdgConfig.getMethodFilter());
    System.out.println("MhpType: " + this.sdgConfig.getMhpType());
    System.out.println("Notifier: " + this.sdgConfig.getNotifier());
    System.out.println("PointsToPrecision: " + this.sdgConfig.getPointsToPrecision());
    System.out.println("PruningPolicy: " + this.sdgConfig.getPruningPolicy());
    System.out.println("SideEffectDetectorConfig: " + this.sdgConfig.getSideEffectDetectorConfig());
    System.out.println("Stubs: " + this.sdgConfig.getStubs());
    System.out.println("SummaryComputationType: " + this.sdgConfig.getSummaryComputationType());
    System.out.println("ThirdPartyLibsPath: " + this.sdgConfig.getThirdPartyLibsPath());
  }

  public SDG build()
      throws GraphIntegrity.UnsoundGraphException, CancelException, ClassHierarchyException,
          IOException {
    long start = System.currentTimeMillis();
    SDGProgram sdgProgram = SDGProgram.createSDGProgram(this.sdgConfig);
    long end = System.currentTimeMillis();
    System.out.println("Built SDG in: " + ((end - start) / 1E3) + " seconds");
    return sdgProgram.getSDG();
  }

  public void save(SDG sdg) throws FileNotFoundException {
    File outputFile = new File(ROOT_DIR + this.programName + "/" + this.programName + DOT_PDG);
    if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
      throw new RuntimeException("Could not create parent directories for " + outputFile);
    }
    SDGSerializer.toPDGFormat(sdg, new PrintWriter(outputFile));
  }
}
