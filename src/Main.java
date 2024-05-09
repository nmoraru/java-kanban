import taskmanager.data.Epic;
import taskmanager.data.Status;
import taskmanager.data.Subtask;
import taskmanager.data.Task;
import taskmanager.manager.FileBackedTaskManager;
import taskmanager.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        //LinkedList<String> list = new LinkedList<>();

        //TaskManager tm = Managers.getDefault();
        TaskManager tm = new FileBackedTaskManager("tasks.csv");
        printAllTasks(tm);

        // Проверки работы с тасками
        System.out.println("Проверки работы с тасками");
        System.out.println("\tИнициализация тасок");
        Task task1 = new Task("task1name", "task1description", 1, Status.DONE);
        Task task2 = new Task("task2name", "task2description", 2, Status.DONE);
        Task task3 = new Task("task3name", "task3description", 3, Status.IN_PROGRESS);

        System.out.println("\tДобавление тасок");
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);

        System.out.println("\tВывод списка созданных тасок");
        System.out.println("\t" + tm.getAllTasks());

//        System.out.println("\tВывод таски по ID");
//        System.out.println("\t" + tm.getTaskToId(1));
//        System.out.println("\t" + tm.getTaskToId(1));
//        System.out.println("\t" + tm.getTaskToId(2));
//        System.out.println("\t" + tm.getTaskToId(1));
//        tm.removeAllTasks();

//
//        System.out.println("\tВывод истории запроса задач");
//        System.out.println("\t" + tm.getHistory());
//
//        System.out.println("\tПроверка обновления таски");
//        Task task2update = new Task("task2update", "task2update", 2, Status.NEW);
//        tm.updateTask(task2update);
//        System.out.println("\t" + tm.getAllTasks());
//
//        System.out.println("\tУдаление таски по ID");
//        tm.removeTaskById(1);
//        System.out.println("\t" + tm.getAllTasks());
//
//        System.out.println("\tПроверка очистки всех тасок");
//        tm.removeAllTasks();
//        System.out.println("\t" + tm.getAllTasks());
//
        // Проверки работы с эпиками
        System.out.println("Проверки работы с эпиками");
        System.out.println("\tИнициализация эпиков");
        Epic epic1 = new Epic("epic1name", "epic1description", 4);
        Epic epic2 = new Epic("epic2name", "epic2description", 5);
        Epic epic3 = new Epic("epic3name", "epic3description", 6);
        Epic epic4 = new Epic("epic4name", "epic4description", 7);

        System.out.println("\tДобавление эпиков");
        tm.createEpic(epic1);
        tm.createEpic(epic2);
        tm.createEpic(epic3);
        tm.createEpic(epic4);

        System.out.println("\tВывод списка созданных эпиков");
        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tВывод эпика по ID");
//        System.out.println("\t" + tm.getEpicToId(4));
//
//        System.out.println("\tВывод несуществующего эпика по ID");
//        System.out.println("\t" + tm.getEpicToId(444));
//
//        System.out.println("\tПроверка обновления эпика");
//        Epic epic1update = new Epic("epic1update", "epic1update", 4);
//        tm.updateEpic(epic1update);
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tУдаление эпика по ID");
//        tm.removeEpicById(7);
//        System.out.println("\t" + tm.getAllEpics());
//
        // Проверки работы с сабтасками
        System.out.println("Проверки работы с сабтасками");
        System.out.println("\tИнициализация сабтасок");
        Subtask subtask1 = new Subtask("subtask1name", "subtask1description", 8, Status.NEW, 4);
        Subtask subtask2 = new Subtask("subtask2name", "subtask2description", 9, Status.IN_PROGRESS, 5);
        Subtask subtask3 = new Subtask("subtask3name", "subtask3description", 10, Status.DONE, 5);
//
//        System.out.println("\tВывод списка созданных эпиков");
//        System.out.println("\t" + tm.getAllEpics());
//description
        System.out.println("\tДобавление сабтасок");
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        tm.createSubtask(subtask3);

        printAllTasks(tm);
//
//        System.out.println("\tВывод всех задач");
//        printAllTasks(tm);
//
//        System.out.println("\tВывод списка созданных сабтасок");
//        System.out.println("\t" + tm.getAllSubtasks());
//        tm.removeAllSubtasks();
//        System.out.println("\tВывод списка созданных сабтасок");
//        System.out.println("\t" + tm.getAllSubtasks());
//
//        System.out.println("\tВывод сабтаски по ID");
//        System.out.println("\t" + tm.getSubtaskToId(8));
//
//        System.out.println("\tВывод списка эпиков");
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tВывод сабтасок по ID эпика");
//        System.out.println("\t" + tm.getSubtasksToEpicId(5));
//
//        System.out.println("\tПроверка обновления сабтаски");
//        Subtask subtask1update = new Subtask("subtask1update", "subtask1update", 8, Status.IN_PROGRESS, 4);
//        tm.updateSubtask(subtask1update);
//        System.out.println("\t" + tm.getAllSubtasks());
//
//        System.out.println("\tВывод списка эпиков");
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tУдаление сабтаски по ID");
//        //tm.removeSubtaskById(9);
//        System.out.println("\t" + tm.getAllSubtasks());
//
//        System.out.println("\tВывод списка эпиков");
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tПроверка очистки всех сабтасок");
//        tm.removeAllSubtasks();
//        System.out.println("\t" + tm.getAllSubtasks());
//
//        System.out.println("\tВывод списка эпиков");
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tПроверка очистки всех эпиков");
//        Subtask subtask4 = new Subtask("subtask4", "subtask4", 11, Status.DONE, 4);
//        tm.createSubtask(subtask4);
//        System.out.println("\t" + tm.getAllEpics());
//        tm.removeAllEpics();
//        System.out.println("\t" + tm.getAllEpics());
//
//        System.out.println("\tВывод списка всех сабтасок");
//        System.out.println("\t" + tm.getAllSubtasks());
//
//        tm.removeAllTasks();
//        System.out.println("\tВывод истории запроса задач");
//        System.out.println("\t" + tm.getHistory());

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
