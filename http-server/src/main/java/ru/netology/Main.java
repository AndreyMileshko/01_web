package ru.netology;

import ru.netology.server.Server;

public class Main {
  public static void main(String[] args) {
    Server server = new Server(8080, 64);

    server.addHandler("GET", "/message", (request, responseStream)
            -> System.out.println("Мы в хэндлере метода GET и пути /message"));
    server.addHandler("POST", "/messages", (request, responseStream)
            -> System.out.println("Мы в хэндлере метода POST и пути /message"));

    server.runServer();
  }
}


