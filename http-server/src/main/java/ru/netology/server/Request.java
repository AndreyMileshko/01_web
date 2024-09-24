package ru.netology.server;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class Request {
    private final String method;
    private final String path;
    private final BufferedReader in;
    private final List<NameValuePair> queryParams;

    private Request(String method, String path, BufferedReader in, List<NameValuePair> queryParams) {
        this.method = method;
        this.path = path;
        this.in = in;
        this.queryParams = queryParams;
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
        final String pathAndQueryString = parts[1];

        if (!pathAndQueryString.contains("?")) {
            return new Request(method, pathAndQueryString, in, null);
        }

        final String[] pathAndQueryStringArray = pathAndQueryString.split("\\?");
        final String path = pathAndQueryStringArray[0];
        final String queryString = pathAndQueryStringArray[1];
        List<NameValuePair> queryParams = URLEncodedUtils.parse(queryString, Charset.defaultCharset());
        return new Request(method, path, in, queryParams);
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public List<NameValuePair> getQueryParam(String name) {
        return queryParams.stream()
                .filter(str -> str.toString().contains(name))
                .collect(Collectors.toList());
    }
}