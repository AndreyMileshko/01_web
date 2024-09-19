package ru.netology.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();
    private final int port;
    private final ExecutorService threadPool;
    private boolean isContinue = true;

    public Server(int port, int threadPoolSize) {
        this.port = port;
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void run() {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while (isContinue) {
                final Socket socket = serverSocket.accept();
                threadPool.submit(() -> RequestHandler.requestHandling(socket, handlers));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.containsKey(method)) {
            handlers.put(method, new ConcurrentHashMap<>());
        }
        handlers.get(method).put(path, handler);
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> getHandlers() {
        return handlers;
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }
}