package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.manager.TaskManager;

import java.io.IOException;

public class PrioritizedTasksHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedTasksHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 2) {
            response = handleGetPrioritizedTasks();
        }
        if (!response.equals("null")) {
            sendText(httpExchange, 200, response);
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetPrioritizedTasks() {
        return gson.toJson(tm.getPrioritizedTasks());
    }
}
