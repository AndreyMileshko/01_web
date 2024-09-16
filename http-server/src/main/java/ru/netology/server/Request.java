package ru.netology.server;

import java.io.BufferedReader;

public class Request {
    private String method;
    private String path;
    private BufferedReader in;

    public Request(String method, String path, BufferedReader in) {
        this.method = method;
        this.path = path;
        this.in = in;
    }
}
