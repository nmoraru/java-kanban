package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager tm;
    protected Gson gson;
    protected String response;
    protected String method;
    protected URI requestURI;
    protected String path;
    protected String[] splitPath;

    public BaseHttpHandler(TaskManager tm, Gson gson) {
        this.tm = tm;
        this.gson = gson;
    }

    protected void sendText(HttpExchange h, Integer respCode, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(respCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendMethodNotAllowed(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(405, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        response = "";
        method = httpExchange.getRequestMethod();
        requestURI = httpExchange.getRequestURI();
        path = requestURI.getPath();
        splitPath = path.split("/");

        switch (method) {
            case "POST":
                processPost(httpExchange);
                break;
            case "DELETE":
                processDelete(httpExchange);
                break;
            case "GET":
                processGet(httpExchange);
                break;
            default:
                sendMethodNotAllowed(httpExchange, "METHOD_NOT_ALLOWED");
        }
    }

    protected void processGet(HttpExchange httpExchange) throws IOException {

    }

    protected void processPost(HttpExchange httpExchange) throws IOException {

    }

    protected void processDelete(HttpExchange httpExchange) throws IOException {

    }
}
