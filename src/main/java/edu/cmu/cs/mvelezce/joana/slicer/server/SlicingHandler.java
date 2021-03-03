package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.cmu.cs.mvelezce.joana.slicer.sdg.read.SDGReader;
import edu.kit.joana.ifc.sdg.graph.SDG;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SlicingHandler implements HttpHandler {

  private final String programName;
  private final SDG sdg;

  public SlicingHandler(String programName) throws IOException {
    this.programName = programName;
    SDGReader reader = new SDGReader(this.programName);
    this.sdg = reader.readSDG();

    System.out.println("Started " + SlicingHandler.class.getSimpleName());
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    byte[] response = "{option: \"will slice\"}".getBytes();
    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
    httpExchange.getResponseBody().write(response);
    httpExchange.close();
  }
}
