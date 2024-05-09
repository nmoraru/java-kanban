package taskmanager.manager;

import taskmanager.data.Epic;
import taskmanager.data.Subtask;
import taskmanager.data.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int currentTaskId = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    public int getNewId() {
        currentTaskId += 1;
        return currentTaskId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getSubtasksToEpicId(int epicId) {
        Epic epic = epicMap.get(epicId);
        return epic.getSubtasksInEpic();
    }

    @Override
    public void createEpic(Epic epic) {
        int newEpicId = getNewId();
        epic.setId(newEpicId);
        epicMap.put(newEpicId, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        int newTaskId = getNewId();
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);

        if (epic != null) {
            subtask.setId(newTaskId);
            subtaskMap.put(newTaskId, subtask);

            epic.addSubtaskToEpic(subtask);
        }
    }

    @Override
    public void createTask(Task task) {
        int newTaskId = getNewId();
        task.setId(newTaskId);

        taskMap.put(newTaskId, task);
    }

    @Override
    public Collection<Task> getAllTasks() {
        return taskMap.values();
    }

    @Override
    public Collection<Epic> getAllEpics() {
        return epicMap.values();
    }

    @Override
    public Collection<Subtask> getAllSubtasks() {
        return subtaskMap.values();
    }

    @Override
    public void removeAllTasks() {
        Set<Integer> idTasks = taskMap.keySet();
        if (!idTasks.isEmpty()) {
            for (Integer id : idTasks) {
                historyManager.remove(id);
            }
        }
        taskMap.clear();
    }

    @Override
    public void removeAllSubtasks() {
        Set<Integer> idSubtasks = subtaskMap.keySet();
        if (!idSubtasks.isEmpty()) {
            for (Integer id : idSubtasks) {
                historyManager.remove(id);
            }
        }

        for (Subtask subtask : subtaskMap.values()) {
            int epicId = subtask.getEpicId();
            Epic epic = epicMap.get(epicId);
            epic.removeSubtaskToEpic(subtask);
        }

        subtaskMap.clear();
    }

    @Override
    public void removeAllEpics() {
        Set<Integer> idEpics = epicMap.keySet();
        if (!idEpics.isEmpty()) {
            for (Integer id : idEpics) {
                historyManager.remove(id);
            }
        }

        for (Epic epic : epicMap.values()) {
            ArrayList<Integer> subtasksInEpicId = new ArrayList<>();
            for (Subtask subtask : epic.getSubtasksInEpic()) {
                subtasksInEpicId.add(subtask.getId());
            }
            for (int subtaskId : subtasksInEpicId) {
                removeSubtaskById(subtaskId);
            }
        }
        epicMap.clear();
    }

    @Override
    public Task getTaskToId(int id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskToId(int id) {
        Subtask subtask = subtaskMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicToId(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        taskMap.put(taskId, task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        int epicId = subtask.getEpicId();
        subtaskMap.put(subtaskId, subtask);

        Epic epic = epicMap.get(epicId);
        epic.addSubtaskToEpic(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        epicMap.put(epicId, epic);
    }

    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        ArrayList<Subtask> subtasksInEpic = getEpicToId(id).getSubtasksInEpic();
        for (Subtask subtask : subtasksInEpic) {
            int subtaskId = subtask.getId();
            subtaskMap.remove(subtask.getId());
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
        epicMap.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);

        historyManager.remove(id);
        subtaskMap.remove(id);
        epic.removeSubtaskToEpic(subtask);
    }

}
