package edu.cmu.cs.mvelezce.joana.slicer.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SlicingHandler implements HttpHandler {

  public SlicingHandler() {
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
