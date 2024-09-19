package ru.netology.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler {

    public static void requestHandling(Socket socket, ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers) {
        try (socket;
             final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {

            Request request = Request.parse(in);

            if (request == null) {
                HttpErrorMessage.badRequest(out);
                return;
            }

            if (!handlers.containsKey(request.getMethod())) {
                HttpErrorMessage.notFound(out);
                return;
            }
            ConcurrentHashMap<String, Handler> handlerMap = handlers.get(request.getMethod());

            if (!handlerMap.containsKey(request.getPath())) {
                HttpErrorMessage.notFound(out);
                return;
            }
            handlerMap.get(request.getPath()).handle(request, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}