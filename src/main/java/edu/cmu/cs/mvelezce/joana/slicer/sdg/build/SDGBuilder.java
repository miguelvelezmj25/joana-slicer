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

  private static final String DOT_PDG = ".pdg";

  private final String programName;
  private final SDGConfig sdgConfig;

  private SDG sdgCache = null;

  public SDGBuilder(String programName, String classPath, String entryMethod) {
    this.programName = programName;
    this.sdgConfig = new SDGConfig(classPath, entryMethod, Stubs.JRE_16);

    this.sdgConfig.setPruningPolicy(PruningPolicy.APPLICATION);
    this.sdgConfig.setComputeInterferences(false);
    this.sdgConfig.setMhpType(MHPType.NONE);
    this.sdgConfig.setExceptionAnalysis(ExceptionAnalysis.IGNORE_ALL);
    this.sdgConfig.setPointsToPrecision(PointsToPrecision.INSTANCE_BASED);
    this.sdgConfig.setFieldPropagation(FieldPropagation.OBJ_GRAPH);

    //    System.out.println("getCGConsumer: " + this.sdgConfig.getCGConsumer());
    //    System.out.println("getClassPath: " + this.sdgConfig.getClassPath());
    //    System.out.println(
    //        "getClasspathAddEntriesFromMANIFEST: "
    //            + this.sdgConfig.getClasspathAddEntriesFromMANIFEST());
    //    System.out.println("getContextSelector: " + this.sdgConfig.getContextSelector());
    //    System.out.println(
    //        "getControlDependenceVariant: " + this.sdgConfig.getControlDependenceVariant());
    //    System.out.println(
    //        "getDefaultExceptionMethodState: " + this.sdgConfig.getDefaultExceptionMethodState());
    //    System.out.println(
    //        "getDynamicDispatchHandling: " + this.sdgConfig.getDynamicDispatchHandling());
    //    System.out.println("getEntryMethod: " + this.sdgConfig.getEntryMethod());
    //    System.out.println("getExceptionAnalysis: " + this.sdgConfig.getExceptionAnalysis());
    //    System.out.println("getExclusions: " + this.sdgConfig.getExclusions());
    //    System.out.println("getFieldHelperOptions: " + this.sdgConfig.getFieldHelperOptions());
    //    System.out.println("getFieldPropagation: " + this.sdgConfig.getFieldPropagation());
    //    System.out.println("getIgnoreIndirectFlows: " + this.sdgConfig.getIgnoreIndirectFlows());
    //    System.out.println("getMethodFilter: " + this.sdgConfig.getMethodFilter());
    //    System.out.println("getMhpType: " + this.sdgConfig.getMhpType());
    //    System.out.println("getNotifier: " + this.sdgConfig.getNotifier());
    //    System.out.println("getPointsToPrecision: " + this.sdgConfig.getPointsToPrecision());
    //    System.out.println("getPruningPolicy: " + this.sdgConfig.getPruningPolicy());
    //    System.out.println(
    //        "getSideEffectDetectorConfig: " + this.sdgConfig.getSideEffectDetectorConfig());
    //    System.out.println("getStubs: " + this.sdgConfig.getStubs());
    //    System.out.println("getSummaryComputationType: " +
    // this.sdgConfig.getSummaryComputationType());
    //    System.out.println("getThirdPartyLibsPath: " + this.sdgConfig.getThirdPartyLibsPath());
  }

  public SDG build()
      throws GraphIntegrity.UnsoundGraphException, CancelException, ClassHierarchyException,
          IOException {
    SDGProgram sdgProgram = SDGProgram.createSDGProgram(this.sdgConfig);
    this.sdgCache = sdgProgram.getSDG();
    return this.sdgCache;
  }

  public void save() throws FileNotFoundException {
    if (this.sdgCache == null) {
      throw new RuntimeException("You need to build a SDG before trying to save it");
    }

    File outputFile = new File("./sdgs/" + programName + DOT_PDG);
    if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
      throw new RuntimeException("Could not create parent directories for " + outputFile);
    }
    SDGSerializer.toPDGFormat(this.sdgCache, new PrintWriter(outputFile));
  }
}
