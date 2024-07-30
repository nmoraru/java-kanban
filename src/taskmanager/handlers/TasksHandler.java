package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.data.Task;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager tm, Gson gson) {
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
            case "POST":
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
                break;
            case "DELETE":
                if (splitPath.length == 3 && tm.getTaskToId(Integer.parseInt(splitPath[2])) != null) {
                    tm.removeTaskById(Integer.parseInt(splitPath[2]));
                    sendText(httpExchange, 201, "Task delete.");
                } else {
                    sendNotFound(httpExchange, "Invalid method.");
                }
                break;
            case "GET":
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
                break;
            default:
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
