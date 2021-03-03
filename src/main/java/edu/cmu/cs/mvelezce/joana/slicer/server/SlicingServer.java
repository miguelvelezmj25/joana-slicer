package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SlicingServer {
  public static final String HOSTNAME = "localhost";
  public static final int PORT = 8002;

  public static void main(String[] args) throws IOException {
    System.out.println(
        "Started "
            + SlicingServer.class.getSimpleName()
            + " on hostname: "
            + HOSTNAME
            + " and port: "
            + PORT);
    HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), 0);
    server.createContext("/slice", new SlicingHandler());
    server.setExecutor(Executors.newFixedThreadPool(1));
    server.start();
  }
}
