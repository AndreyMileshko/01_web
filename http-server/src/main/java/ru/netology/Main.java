package ru.netology;

import ru.netology.server.Request;
import ru.netology.server.Server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080, 64);

        server.addHandler("GET", "/message", (request, responseStream)
                -> {
            String responseBody = "Hello from message endpoint!";
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plaine" + "\r\n" +
                            "Content-Length: " + responseBody.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(responseBody.getBytes());
            responseStream.flush();
        });

        server.addHandler("GET", "/spring.svg", (Main::handleFile));
        server.addHandler("GET", "/spring.png", (Main::handleFile));

        server.run();
    }

    public static void handleFile(Request request, BufferedOutputStream responseStream) throws IOException {
        final Path filePath = Path.of(".", "public", request.getPath());           //C:\Users\andre\Desktop
        final String mimeType = Files.probeContentType(filePath);

        final long length = Files.size(filePath);
        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, responseStream);
        responseStream.flush();
    }
}