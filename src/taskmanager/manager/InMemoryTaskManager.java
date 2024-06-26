package taskmanager.manager;

import taskmanager.data.Epic;
import taskmanager.data.Subtask;
import taskmanager.data.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int currentTaskId = 0;
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Set prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public int getNewId() {
        currentTaskId += 1;
        return currentTaskId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return epicMap.values()
                .stream()
                .filter(epic -> epic.getId() == epicId)
                .map(Epic::getSubtasksInEpic).toList()
                .get(0)
                ;
    }

    @Override
    public void createEpic(Epic epic) {
        int newEpicId = getNewId();
        epic.setId(newEpicId);
        epicMap.put(newEpicId, epic);
    }

    @Override
    public void createTask(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            if (isBusyTimeForTask(task)) {
                return;
            }
        }
        int newTaskId = getNewId();
        task.setId(newTaskId);
        taskMap.put(newTaskId, task);
        if (task.getStartTime() != null && task.getDuration() != null) {
            prioritizedTasks.add(task);
        }

    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            if (isBusyTimeForTask(subtask)) {
                return;
            }
        }
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);

        if (epic != null) {
            int newTaskId = getNewId();
            subtask.setId(newTaskId);
            subtaskMap.put(newTaskId, subtask);
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                prioritizedTasks.add(subtask);
            }
            epic.addSubtaskToEpic(subtask);
        }

    }

    private boolean isBusyTimeForTask(Task task) {
        if (!prioritizedTasks.isEmpty()) {
            for (Task prioritizedTask : getPrioritizedTasks()) {
                if (!isBeforeOtherTask(prioritizedTask, task)) {
                    if (!isAfterOtherTask(prioritizedTask, task)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAfterOtherTask(Task t1, Task t2) {
        return t1.getStartTime().isAfter(t2.getEndTime());
    }

    private boolean isBeforeOtherTask(Task t1, Task t2) {
        return t1.getEndTime().isBefore(t2.getStartTime());
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
        prioritizedTasks.removeAll(getAllTasks());
        taskMap.clear();
    }

    @Override
    public void removeAllSubtasks() {
        Set<Integer> idSubtasks = subtaskMap.keySet();
        if (!idSubtasks.isEmpty()) {
            for (Integer id : idSubtasks) {
                prioritizedTasks.remove(getSubtaskToId(id));
                historyManager.remove(id);
                removeSubtaskById(id);
            }
        }
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
        return taskMap.values().stream()
                .filter(task -> task.getId() == id)
                .peek(historyManager::add).toList().get(0);
    }

    @Override
    public Subtask getSubtaskToId(int id) {
        return subtaskMap.values().stream()
                .filter(task -> task.getId() == id)
                .peek(historyManager::add).toList().get(0);
    }

    @Override
    public Epic getEpicToId(int id) {
        return epicMap.values().stream()
                .filter(task -> task.getId() == id)
                .peek(historyManager::add).toList().get(0);
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(task);
        if (isBusyTimeForTask(task)) {
            return;
        }
        int taskId = task.getId();
        taskMap.put(taskId, task);

        if (task.getStartTime() != null && task.getDuration() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        prioritizedTasks.remove(subtask);
        if (isBusyTimeForTask(subtask)) {
            return;
        }
        int subtaskId = subtask.getId();
        int epicId = subtask.getEpicId();
        subtaskMap.put(subtaskId, subtask);

        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            prioritizedTasks.add(subtask);
        }

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
        prioritizedTasks.remove(getTaskToId(id));
        historyManager.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        ArrayList<Subtask> subtasksInEpic = getEpicToId(id).getSubtasksInEpic();
        for (Subtask subtask : subtasksInEpic) {
            int subtaskId = subtask.getId();
            prioritizedTasks.remove(getSubtaskToId(subtaskId));
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

        prioritizedTasks.remove(id);
        historyManager.remove(id);
        subtaskMap.remove(id);
        epic.removeSubtaskToEpic(subtask);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
