package taskmanager.manager;

import taskmanager.data.Epic;
import taskmanager.data.Subtask;
import taskmanager.data.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    ArrayList<Subtask> getSubtasksToEpicId(int epicId);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void createTask(Task task);

    Collection<Task> getAllTasks();

    Collection<Epic> getAllEpics();

    Collection<Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    Task getTaskToId(int id);

    Subtask getSubtaskToId(int id);

    Epic getEpicToId(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);
}
