package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.data.Subtask;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 2) {
            response = handleGetAllSubtasks();
        }
        if (splitPath.length == 3) {
            response = handleGetSubtaskToId(Integer.parseInt(splitPath[2]));
        }
        if (!response.equals("null")) {
            sendText(httpExchange, 200, response);
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    @Override
    protected void processPost(HttpExchange httpExchange) throws IOException {
        InputStream is = httpExchange.getRequestBody();
        String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Subtask newTask = gson.fromJson(resp, Subtask.class);

        if (splitPath.length == 2) {
            if (tm.getSubtaskToId(newTask.getId()) == null) {
                tm.createSubtask(newTask);
                if (tm.getSubtaskToId(newTask.getId()) != null) {
                    sendText(httpExchange, 201, "Subtask create.");
                } else {
                    sendHasInteractions(httpExchange, "Subtask not create. Is busy time for subtask.");
                }
            } else {
                sendNotFound(httpExchange, "Subtask not created.");
            }
        } else if (splitPath.length == 3) {
            if (tm.getSubtaskToId(newTask.getId()) != null) {
                tm.updateSubtask(newTask);
                if (tm.getSubtaskToId(newTask.getId()) != null) {
                    sendText(httpExchange, 201, "Subtask update.");
                } else {
                    sendHasInteractions(httpExchange, "Subtask not update. Is busy time for subtask.");
                }
            } else {
                sendNotFound(httpExchange, "Subtask not update.");
            }
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    @Override
    protected void processDelete(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 3 && tm.getSubtaskToId(Integer.parseInt(splitPath[2])) != null) {
            tm.removeSubtaskById(Integer.parseInt(splitPath[2]));
            sendText(httpExchange, 201, "Subtask delete.");
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetAllSubtasks() {
        return gson.toJson(tm.getAllSubtasks());
    }

    public String handleGetSubtaskToId(int taskId) {
        return gson.toJson(tm.getSubtaskToId(taskId));
    }
}
