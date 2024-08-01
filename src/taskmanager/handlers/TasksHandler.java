package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.data.Task;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 2) {
            response = handleGetAllTasks();
        }
        if (splitPath.length == 3) {
            response = handleGetTaskToId(Integer.parseInt(splitPath[2]));
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
        Task newTask = gson.fromJson(resp, Task.class);
        System.out.println(newTask);

        if (splitPath.length == 2) {
            if (tm.getTaskToId(newTask.getId()) == null) {
                tm.createTask(newTask);
                if (tm.getTaskToId(newTask.getId()) != null) {
                    sendText(httpExchange, 201, "Task create.");
                } else {
                    sendHasInteractions(httpExchange, "Task not create. Is busy time for task.");
                }

            } else {
                sendNotFound(httpExchange, "Task not created.");
            }

        } else if (splitPath.length == 3) {
            int parseTaskId = Integer.parseInt(splitPath[2]);
            if (tm.getTaskToId(parseTaskId) != null
                    && parseTaskId == newTask.getId()) {
                tm.updateTask(newTask);
                if (tm.getTaskToId(newTask.getId()) != null) {
                    sendText(httpExchange, 201, "Task update.");
                } else {
                    sendHasInteractions(httpExchange, "Task not update. Is busy time for task.");
                }
            } else {
                sendNotFound(httpExchange, "Task not update.");
            }
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    @Override
    protected void processDelete(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 3 && tm.getTaskToId(Integer.parseInt(splitPath[2])) != null) {
            tm.removeTaskById(Integer.parseInt(splitPath[2]));
            sendText(httpExchange, 201, "Task delete.");
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetAllTasks() {
        return gson.toJson(tm.getAllTasks());
    }

    public String handleGetTaskToId(int taskId) {
        return gson.toJson(tm.getTaskToId(taskId));
    }
}
