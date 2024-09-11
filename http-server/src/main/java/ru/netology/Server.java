package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.netology.RequestHandler.*;

public class Server {
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

    public void setCont(boolean cont) {
        this.cont = cont;
    }
}
