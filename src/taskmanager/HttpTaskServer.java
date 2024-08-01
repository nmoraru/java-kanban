package taskmanager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import taskmanager.data.Task;
import taskmanager.handlers.*;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import static taskmanager.manager.FileBackedTaskManager.loadFromFile;

public class HttpTaskServer {

    private static final int PORT = 8080;
    static TaskManager tm;
    static HttpServer httpServer;

    public HttpTaskServer(TaskManager tm) throws IOException {
        this.tm = tm;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public static void main(String[] args) throws IOException {

        String path = "resources/data.csv";
        File file = new File(path);
        tm = loadFromFile(file);
        printAllTasks(tm);
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        start(tm);

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void start(TaskManager tm) {
        Gson gson = Managers.getGson();
        httpServer.createContext("/tasks", new TasksHandler(tm, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(tm, gson));
        httpServer.createContext("/epics", new EpicsHandler(tm, gson));
        httpServer.createContext("/history", new HistoryHandler(tm, gson));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(tm, gson));
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(2);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
