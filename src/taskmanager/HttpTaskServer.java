package taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import taskmanager.data.Task;
import taskmanager.handlers.*;
import taskmanager.jsonadapters.DurationAdapter;
import taskmanager.jsonadapters.LocalDateTimeAdapter;
import taskmanager.manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import static taskmanager.manager.FileBackedTaskManager.loadFromFile;

public class HttpTaskServer {

    private static final int PORT = 8080;
    static TaskManager tm;
    static HttpServer httpServer;
    static GsonBuilder gsonBuilder = new GsonBuilder();
    static Gson gson;

    public HttpTaskServer(TaskManager tm) throws IOException {
        this.tm = tm;
        gson = gsonBuilder.setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {

        String path = "resources/data.csv";
        File file = new File(path);
        tm = loadFromFile(file);
        printAllTasks(tm);
        gson = gsonBuilder.setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        start(tm);

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    protected static void start(TaskManager tm) {
        httpServer.createContext("/tasks", new TasksHandler(tm, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(tm, gson));
        httpServer.createContext("/epics", new EpicsHandler(tm, gson));
        httpServer.createContext("/history", new HistoryHandler(tm, gson));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(tm, gson));
        httpServer.start();
    }

    protected static void stop() {
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
