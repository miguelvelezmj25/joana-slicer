package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    Map<String, Integer> stmtsToNotHighlight = getStmtsToNotHighlight(programName);
    HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, port), 0);
    server.createContext(
        "/slice", new SlicingHandler(programName, excludedMethods, stmtsToNotHighlight));
    server.setExecutor(Executors.newFixedThreadPool(1));
    server.start();
  }

  private static Map<String, Integer> getStmtsToNotHighlight(String programName) {
    Map<String, Integer> stmtsToNotHighlight = new HashMap<>();
    if (programName.equals("example")) {
      //      stmtsToNotHighlight.put("edu.cmu.cs.mvelezce.perf.debug.config.core.Main.main", 10);
    }
    return stmtsToNotHighlight;
  }

  private static Set<String> getExcludedMethods(String programName) {
    Set<String> excludedMethods = new HashSet<>();
    if (programName.equals("example")) {
      //      excludedMethods.add("edu.cmu.cs.mvelezce.perf.debug.config.core.TaskHandler.<init>");
    }
    return excludedMethods;
  }
}
