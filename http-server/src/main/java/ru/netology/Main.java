package ru.netology;

import ru.netology.server.Handler;
import ru.netology.server.Request;
import ru.netology.server.Server;

import java.io.BufferedOutputStream;

public class Main {
  public static void main(String[] args) {
    Server server = new Server(8080, 64);

    server.addHandler("GET", "/message", new Handler() {
      @Override
      public void handle(Request request, BufferedOutputStream responseStream) {

      }
    });
    server.addHandler("POST", "/messages", new Handler() {
      public void handle(Request request, BufferedOutputStream responseStream) {

      }
    });

    server.runServer();
  }
}


