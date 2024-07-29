import com.sun.net.httpserver.HttpServer;
import taskmanager.data.Task;
import taskmanager.handlers.*;
import taskmanager.manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import static taskmanager.manager.FileBackedTaskManager.loadFromFile;

public class HttpTaskServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        String path = "resources/data.csv";
        File file = new File(path);
        TaskManager tm = loadFromFile(file);
        printAllTasks(tm);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(tm));
        httpServer.createContext("/subtasks", new SubtasksHandler(tm));
        httpServer.createContext("/epics", new EpicsHandler(tm));
        httpServer.createContext("/history", new HistoryHandler(tm));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(tm));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        //httpServer.stop(2);
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
