package taskmanager.manager;

import taskmanager.data.Epic;
import taskmanager.data.Status;
import taskmanager.data.Subtask;
import taskmanager.data.Task;
import taskmanager.exception.ManagerLoadException;
import taskmanager.exception.ManagerSaveException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public static Task fromString(String value) {
        String[] itemParams = value.split(",");

        int id = Integer.parseInt(itemParams[0]);
        String name = itemParams[2];
        String description = itemParams[4];
        Status status;
        String type = itemParams[1];

        if (itemParams[3].equals("NEW")) {
            status = Status.NEW;
        } else if (itemParams[3].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }

        if (type.equals("TASK")) {
            Task task = new Task(
                    name,
                    description,
                    id,
                    status
            );
            return task;
        }

        if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(itemParams[5]);

            Subtask subtask = new Subtask(
                    name,
                    description,
                    id,
                    status,
                    epicId
            );
            return subtask;
        }

        if (type.equals("EPIC")) {
            Epic epic = new Epic(
                    name,
                    description,
                    id
            );
            return epic;
        }

        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager tm = Managers.getFileBackedTaskManager(file);

        try {
            try {
                String[] items = Files.readString(file.toPath()).split("\n");
                if (items.length == 1) {
                    return tm;
                }

                for (int i = 1; i < items.length; i++) {
                    Task task = fromString(items[i]);
                    switch (task.getType()) {
                        case TASK -> tm.createTask(task);
                        case EPIC -> tm.createEpic((Epic) task);
                        case SUBTASK -> tm.createSubtask((Subtask) task);
                    }
                }
                return tm;
            } catch (IOException e) {
                throw new ManagerLoadException();
            }
        } catch (ManagerLoadException e) {
            System.out.println(e.getMessage());
            return tm;
        }
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
