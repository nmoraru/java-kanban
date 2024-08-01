package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 2) {
            response = handleGetHistory();
        }
        if (!response.equals("null")) {
            sendText(httpExchange, 200, response);
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetHistory() {
        return gson.toJson(tm.getHistory());
    }
}
