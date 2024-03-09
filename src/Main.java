import taskManager.data.Epic;
import taskManager.data.Status;
import taskManager.data.Subtask;
import taskManager.data.Task;
import taskManager.manager.TaskManager;

import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        System.out.println("Создание задач");
        Task task1 = new Task("name3", "desc3", 1, Status.DONE);
        Task task2 = new Task("name3", "desc3", 2, Status.DONE);
        Task epic1 = new Epic("name3", "desc3", 3, Status.DONE, new TreeSet<>());
        Task epic2 = new Epic("name2", "desc2", 4, Status.NEW, new TreeSet<>());
        Task subtask1 = new Subtask("name2", "desc2", 5, Status.NEW, 3);
        Task subtask2 = new Subtask("name2", "desc2", 6, Status.IN_PROGRESS, 4);
        Task subtask3 = new Subtask("name2", "desc2", 7, Status.DONE, 4);

        //tm.createTask(subtask1);

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(epic1);
        tm.createTask(epic2);
        tm.createTask(subtask1);
        tm.createTask(subtask2);
        tm.createTask(subtask3);

        System.out.println("Вывод списка созданных задач и состава двух эпиков");
        System.out.println(tm.getSubtasksForEpic(3));
        System.out.println(tm.getSubtasksForEpic(4));
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка обновления таски");
        Task task2update = new Task("task2update", "task2update", 2, Status.NEW);
        tm.updateTask(task2update);
        System.out.println(tm.getAllTasks());

        System.out.println("Удаление двух тасок по их ID");
        tm.removeById(1);
        tm.removeById(2);
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка обновления эпика");
        Task epic1update = new Epic("epic1update", "epic1update", 3, Status.NEW, new TreeSet<>());
        tm.updateTask(epic1update);
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка обновления сабтаски");
        Task subtask1update = new Subtask("subtask1update", "subtask1update", 5, Status.IN_PROGRESS, 3);
        tm.updateTask(subtask1update);
        Task subtask2update = new Subtask("subtask1update", "subtask1update", 6, Status.IN_PROGRESS, 3);
        tm.updateTask(subtask2update);
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка удаления эпика");
        tm.removeById(4);
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка удаления всех сабтасок из эпика");
        tm.removeById(6);
        System.out.println(tm.getAllTasks());
        tm.removeById(5);
        System.out.println(tm.getAllTasks());

        System.out.println("Проверка очистки списка задач");
        tm.removeAllTasks();
        System.out.println(tm.getAllTasks());
    }
}
