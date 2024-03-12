package taskManager.manager;

import taskManager.data.Epic;
import taskManager.data.Subtask;
import taskManager.data.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private static int currentTaskId = 0;

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private int getNewId() {
        currentTaskId += 1;
        return currentTaskId;
    }

    public ArrayList<Subtask> getSubtasksToEpicId(int epicId) {
        Epic epic = epicMap.get(epicId);
        return epic.getSubtasksInEpic();
    }

    public void createEpic(Epic epic) {
        int newEpicId = getNewId();
        epic.setId(newEpicId);
        epicMap.put(newEpicId, epic);
    }

    public void createSubtask(Subtask subtask) {
        int newTaskId = getNewId();
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);

        subtask.setId(newTaskId);
        subtaskMap.put(newTaskId, subtask);
        epic.addSubtaskToEpic(subtask);
    }

    public void createTask(Task task) {
        int newTaskId = getNewId();
        task.setId(newTaskId);

        taskMap.put(newTaskId, task);
    }

    public Collection<Task> getAllTasks() {
        return taskMap.values();
    }

    public Collection<Epic> getAllEpics() {
        return epicMap.values();
    }

    public Collection<Subtask> getAllSubtasks() {
        return subtaskMap.values();
    }

    public void removeAllTasks() {
        taskMap.clear();
    }

    public void removeAllSubtasks() {
        for (Subtask subtask : subtaskMap.values()) {
            int epicId = subtask.getEpicId();
            removeEpicById(epicId);
        }
    }

    public void removeAllEpics() {
        for (Epic epic : epicMap.values()) {
            removeEpicById(epic.getId());
        }
    }

    public Task getTaskToId(int id) {
        return taskMap.get(id);
    }

    public Subtask getSubtaskToId(int id) {
        return subtaskMap.get(id);
    }

    public Epic getEpicToId(int id) {
        return epicMap.get(id);
    }

    public void updateTask(Task task) {
        int taskId = task.getId();
        taskMap.put(taskId, task);
    }

    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        int epicId = subtask.getEpicId();
        subtaskMap.put(subtaskId, subtask);

        Epic epic = epicMap.get(epicId);
        epic.addSubtaskToEpic(subtask);
    }
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        epicMap.put(epicId, epic);
    }

    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    public void removeEpicById(int id) {
        ArrayList<Subtask> subtasksInEpic = getEpicToId(id).getSubtasksInEpic();
        for (Subtask subtask : subtasksInEpic) {
            subtaskMap.remove(subtask);
        }
        epicMap.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);

        subtaskMap.remove(id);
        epic.removeSubtaskToEpic(subtask);
    }

}
