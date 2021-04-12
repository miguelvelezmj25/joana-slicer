package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class SlicingServer {
  public static final String HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    int port = Integer.parseInt(args[1]);
    System.out.println(
        "Started "
            + SlicingServer.class.getSimpleName()
            + " on hostname: "
            + HOSTNAME
            + " and port: "
            + port
            + " for program: "
            + programName);
    Set<String> excludedMethods = getExcludedMethods(programName);
    Map<String, Set<Integer>> stmtsToNotHighlight = getStmtsToNotHighlight(programName);
    HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, port), 0);
    server.createContext(
        "/slice", new SlicingHandler(programName, excludedMethods, stmtsToNotHighlight));
    server.setExecutor(Executors.newFixedThreadPool(1));
    server.start();
  }

  private static Map<String, Set<Integer>> getStmtsToNotHighlight(String programName) {
    Map<String, Set<Integer>> stmtsToNotHighlight = new HashMap<>();
    if (programName.equals("example")) {
      //      stmtsToNotHighlight.put("edu.cmu.cs.mvelezce.perf.debug.config.core.Main.main", 10);
    } else if (programName.equals("density")) {
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.DConvert.1.onFinished",
          new HashSet<>(Collections.singletonList(180)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.WorkerHandler.start",
          new HashSet<>(Collections.singletonList(84)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.converters.APlatformConverter.convert",
          new HashSet<>(Arrays.asList(42, 46, 72, 78, 82)));
      stmtsToNotHighlight.put(
          "com.mortennobel.imagescaling.ResampleOp.doFilter",
          new HashSet<>(Arrays.asList(111, 114, 115, 116, 118, 120, 124, 125)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.converters.scaling.ImageHandler.saveToFile",
          new HashSet<>(Arrays.asList(67, 69, 86, 88, 89)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.converters.scaling.ImageHandler.scale",
          new HashSet<>(Arrays.asList(204, 212)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.util.MiscUtil.createAndCheckFolder",
          new HashSet<>(Arrays.asList(54, 55, 62, 65)));
      //      stmtsToNotHighlight.put(
      //          "com.mortennobel.imagescaling", new HashSet<>(Collections.singletonList(72)));
      stmtsToNotHighlight.put(
          "at.favre.tools.dconvert.util.DensityBucketUtil.getDensityBuckets",
          new HashSet<>(Arrays.asList(43, 44)));
    }
    return stmtsToNotHighlight;
  }

  private static Set<String> getExcludedMethods(String programName) {
    Set<String> excludedMethods = new HashSet<>();
    if (programName.equals("example")) {
      //      excludedMethods.add("edu.cmu.cs.mvelezce.perf.debug.config.core.TaskHandler.<init>");
    } else if (programName.equals("density")) {
      excludedMethods.add("at.favre.tools.dconvert.DConvert.1.onFinished");
      excludedMethods.add("java.util.TreeMap.put");
      excludedMethods.add("com.mortennobel.imagescaling.ImageUtils.nrChannels");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.AndroidConverter.createFolderForOutputFile");
      excludedMethods.add("at.favre.tools.dconvert.util.MiscUtil.createAndCheckFolder");
      excludedMethods.add("at.favre.tools.dconvert.converters.AndroidConverter.isNinePatch");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.AndroidConverter.usedOutputDensities");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.AndroidConverter.getAndroidDensityDescriptors");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.descriptors.AndroidDensityDescriptor.<init>");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.descriptors.DensityDescriptor.<init>");
      excludedMethods.add("at.favre.tools.dconvert.util.ImageUtil.loadImage");
      excludedMethods.add("at.favre.tools.dconvert.util.ImageUtil.read");
      excludedMethods.add(
          "at.favre.tools.dconvert.converters.descriptors.DensityDescriptor.compareTo");
    }
    return excludedMethods;
  }
}
