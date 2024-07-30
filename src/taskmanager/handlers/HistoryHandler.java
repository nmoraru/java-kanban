package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.net.URI;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitPath = path.split("/");

        switch (method) {
            case "GET":
                if (splitPath.length == 2) {
                    response = handleGetHistory();
                }
                if (!response.equals("null")) {
                    sendText(httpExchange, 200, response);
                } else {
                    sendNotFound(httpExchange, "Invalid method.");
                }
                break;
            default:
                sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetHistory() {
        return gson.toJson(tm.getHistory());
    }
}
