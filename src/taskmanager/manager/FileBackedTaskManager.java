package taskmanager.manager;

import taskmanager.data.Epic;
import taskmanager.data.Subtask;
import taskmanager.data.Task;
import taskmanager.exception.ManagerSaveException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String filePath;

    public FileBackedTaskManager(String filePath) {
        this.filePath = filePath;
    }

    private void save() {
        Map<Integer, Task> allBacklog = new HashMap<>();
        allBacklog.putAll(taskMap);
        allBacklog.putAll(epicMap);
        allBacklog.putAll(subtaskMap);

        try {
            try (FileWriter fw = new FileWriter(filePath)) {
                fw.write("id,type,name,status,description,epic\n");
                for (Task element : allBacklog.values()) {
                    fw.write(toString(element) + "\n");
                }
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private String toString(Task task) {
        switch (task.getType()) {
            case SUBTASK:
                return String.join(",",
                        Integer.toString(task.getId()),
                        task.getType().toString(),
                        task.getName(),
                        task.getStatus().toString(),
                        task.getDescription(),
                        Integer.toString(((Subtask) task).getEpicId()));
            case TASK, EPIC:
                return String.join(",",
                        Integer.toString(task.getId()),
                        task.getType().toString(),
                        task.getName(),
                        task.getStatus().toString(),
                        task.getDescription(),
                        "");
        }
        return null;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }
}
