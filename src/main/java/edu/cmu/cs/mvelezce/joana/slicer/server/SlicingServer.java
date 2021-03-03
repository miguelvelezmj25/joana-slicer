package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, port), 0);
    server.createContext("/slice", new SlicingHandler(programName));
    server.setExecutor(Executors.newFixedThreadPool(1));
    server.start();
  }
}
