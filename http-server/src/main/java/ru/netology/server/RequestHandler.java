package ru.netology.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class RequestHandler {

    private static final List<String> VALID_PATHS = List.of("/index.html", "/spring.svg",
            "/spring.png", "/resources.html", "/styles.css",
            "/app.js", "/links.html", "/forms.html",
            "/classic.html", "/events.html", "/events.js", "/events.js");

//    public static void pathValidation(Socket socket) {
//        try (socket;
//             final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
//            // read only request line for simplicity
//            // must be in form GET /path HTTP/1.1
//            final var requestLine = in.readLine();
//            final var parts = requestLine.split(" ");
//
//            if (parts.length != 3) {
//                // just close socket
//                return;
//            }
//
//            final var path = parts[1];
//            if (!VALID_PATHS.contains(path)) {
//                out.write((
//                        "HTTP/1.1 404 Not Found\r\n" +
//                                "Content-Length: 0\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                out.flush();
//                return;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void requestHandling(Socket socket) {
        try(socket;
            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {

            final String requestLine = in.readLine();
            final String[] parts = requestLine.split(" ");
            if (parts.length != 3) return;

            String method = parts[0];
            String path = parts[1];
            Request request = new Request(method, path, in);

            if (Server.getHandlers().containsKey(method)) {
                if (Server.getHandlers().get(method).containsKey(path)) {
                    Server.getHandlers().get(method).get(path).handle(request, out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




//        try (socket;
//             final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
//            // read only request line for simplicity
//            // must be in form GET /path HTTP/1.1
//            final var requestLine = in.readLine();
//            final var parts = requestLine.split(" ");
//
//            if (parts.length != 3) {
//                // just close socket
//                return;
//            }
//
//            final var path = parts[1];
//            if (!VALID_PATHS.contains(path)) {
//                out.write((
//                        "HTTP/1.1 404 Not Found\r\n" +
//                                "Content-Length: 0\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                out.flush();
//                return;
//            }
//
//            final var filePath = Path.of("C:\\Users\\andre\\Desktop", "public", path);
//            final var mimeType = Files.probeContentType(filePath);
//
//            // special case for classic
//            if (path.equals("/classic.html")) {
//                final var template = Files.readString(filePath);
//                final var content = template.replace(
//                        "{time}",
//                        LocalDateTime.now().toString()
//                ).getBytes();
//                out.write((
//                        "HTTP/1.1 200 OK\r\n" +
//                                "Content-Type: " + mimeType + "\r\n" +
//                                "Content-Length: " + content.length + "\r\n" +
//                                "Connection: close\r\n" +
//                                "\r\n"
//                ).getBytes());
//                out.write(content);
//                out.flush();
//                return;
//            }
//
//            final var length = Files.size(filePath);
//            out.write((
//                    "HTTP/1.1 200 OK\r\n" +
//                            "Content-Type: " + mimeType + "\r\n" +
//                            "Content-Length: " + length + "\r\n" +
//                            "Connection: close\r\n" +
//                            "\r\n"
//            ).getBytes());
//            Files.copy(filePath, out);
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
