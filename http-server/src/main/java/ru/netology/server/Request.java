package ru.netology.server;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private final String method;
    private final String path;
    private final BufferedReader in;

    public Request(String method, String path, BufferedReader in) {
        this.method = method;
        this.path = path;
        this.in = in;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public BufferedReader getIn() {
        return in;
    }

    public static Request parse(BufferedReader in) throws IOException {
        final String requestLine = in.readLine();
        final String[] parts = requestLine.split(" ");

        if (parts.length != 3)
            return null;

        final String method = parts[0];
        final String path = parts[1];
        return new Request(method, path, in);
    }
}