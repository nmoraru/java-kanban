import taskmanager.data.Task;
import taskmanager.manager.TaskManager;

import java.io.File;

import static taskmanager.manager.FileBackedTaskManager.loadFromFile;

public class Main {

    public static void main(String[] args) {
        String path = "resources/data.csv";
        File file = new File(path);

        TaskManager tm = loadFromFile(file);
        printAllTasks(tm);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksToEpicId(epic.getId())) {
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
