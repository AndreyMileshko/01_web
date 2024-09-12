package ru.netology.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.netology.server.RequestHandler.*;

public class Server {

    private final Map<String, Map<String, Handler>> handlers = new HashMap<>();
    private final int port;
    private final ExecutorService threadPool;
    private boolean cont = true;

    public Server(int port, int threadPoolSize) {
        this.port = port;
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void runServer() {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while (cont) {
                final Socket socket = serverSocket.accept();
                threadPool.submit(() -> requestHandling(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHandler(String method, String path, Handler handler) {
        handlers.put(method, new HashMap<>());
        handlers.get(method).put(path, handler);
    }

    public void setCont(boolean cont) {
        this.cont = cont;
    }
}
