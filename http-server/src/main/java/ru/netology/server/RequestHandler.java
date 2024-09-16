package ru.netology.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class RequestHandler {
    private static final List<String> VALID_PATHS = List.of("/index.html", "/spring.svg",
            "/spring.png", "/resources.html", "/styles.css",
            "/app.js", "/links.html", "/forms.html",
            "/classic.html", "/events.html", "/events.js", "/events.js");

    public static void requestHandling(Socket socket) {
        try (socket;
             final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) return;

            final String method = parts[0];
            final String path = parts[1];

            if (VALID_PATHS.contains(path)) {
                requestHandlingVALID_PATHS(out, path);
            } else if (Server.getHandlers().containsKey(method)){
                if (Server.getHandlers().get(method).containsKey(path)) {
                    Request request = new Request(method, path, in);
                    Server.getHandlers().get(method).get(path).handle(request, out);
                }
            } else {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestHandlingVALID_PATHS(BufferedOutputStream out, String path) throws IOException {
        final Path filePath = Path.of(".", "public", path);                 //C:\Users\andre\Desktop
        final String mimeType = Files.probeContentType(filePath);

        if (path.equals("/classic.html")) {
            final String template = Files.readString(filePath);
            final byte[] content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.write(content);
            out.flush();
            return;
        }

        final long length = Files.size(filePath);
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }
}