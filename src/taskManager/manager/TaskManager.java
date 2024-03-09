package taskManager.manager;
import taskManager.data.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class TaskManager {
    private static int currentTaskId = 0;

    HashMap<Integer, Task> taskMap = new HashMap<>();

    private int getNewTaskId() {
        currentTaskId += 1;
        return currentTaskId;
    }

    public void createTask(Task task) {
        int newTaskId = getNewTaskId();
        task.setId(newTaskId);

        taskMap.put(newTaskId, task);

        if (task.getClass() == Epic.class) {
            updateTask(task);
        }

        if (task.getClass() == Subtask.class) {
            int epicId = ((Subtask) task).getEpicId();
            TreeSet<Integer> newList = getSubtasksForEpic(epicId);
            Epic currentEpic = (Epic) getTaskToId(epicId);
            newList.add(newTaskId);
            updateTask(new Epic(
                    currentEpic.getName(),
                    currentEpic.getDescription(),
                    currentEpic.getId(),
                    currentEpic.getStatus(),
                    newList
            ));
        }
    }

    public Collection<Task> getAllTasks() {
        return taskMap.values();
    }

    public void removeAllTasks() {
        taskMap.clear();
    }

    public Task getTaskToId(int id) {
        return taskMap.get(id);
    }

    public void updateTask(Task task) {
        int taskId = task.getId();

        if (task.getClass() == Subtask.class) {
            int oldEpicId = ((Subtask) getTaskToId(taskId)).getEpicId();
            int newEpicId = ((Subtask) task).getEpicId();
            Epic oldEpic = (Epic) getTaskToId(oldEpicId);
            Epic newEpic = (Epic) getTaskToId(newEpicId);
            TreeSet<Integer> oldEpicListTasks = getSubtasksForEpic(oldEpicId);
            TreeSet<Integer> newEpicListTasks = getSubtasksForEpic(newEpicId);

            taskMap.put(taskId, task);

            if (oldEpicId != newEpicId) {
                oldEpicListTasks.remove(taskId);
                updateTask(new Epic(
                        oldEpic.getName(),
                        oldEpic.getDescription(),
                        oldEpic.getId(),
                        oldEpic.getStatus(),
                        oldEpicListTasks
                ));
            }

            newEpicListTasks.add(taskId);
            updateTask(new Epic(
                    newEpic.getName(),
                    newEpic.getDescription(),
                    newEpic.getId(),
                    newEpic.getStatus(),
                    newEpicListTasks
            ));
            updateTask(newEpic);
        }

        if (task.getClass() == Epic.class) {
            Status newEpicStatus = updateEpicStatus(taskId);
            task = new Epic(
                    task.getName(),
                    task.getDescription(),
                    taskId,
                    newEpicStatus,
                    getSubtasksForEpic(taskId));
        }

        taskMap.put(taskId, task);
    }

    public void removeById(int id) {
        if (getTaskToId(id).getClass() == Epic.class) {
            TreeSet<Integer> subtasksId = ((Epic) getTaskToId(id)).getSubtasksId();
            for (int subtaskId : subtasksId) {
                taskMap.remove(subtaskId);
            }
        }

        if (getTaskToId(id).getClass() == Subtask.class) {
            int epicId = ((Subtask) getTaskToId(id)).getEpicId();
            Epic epic = (Epic) getTaskToId(epicId);
            TreeSet<Integer> listSubtasksId = epic.getSubtasksId();
            listSubtasksId.remove(id);
            updateTask(new Epic(
                    epic.getName(),
                    epic.getDescription(),
                    epicId,
                    epic.getStatus(),
                    listSubtasksId
            ));
        }

        taskMap.remove(id);

    }

    public TreeSet<Integer> getSubtasksForEpic(int id) {
        return ((Epic) taskMap.get(id)).getSubtasksId();
    }

    private Status updateEpicStatus(int id) {
        Status status = Status.NEW;
        TreeSet<Integer> subtasks = getSubtasksForEpic(id);
        int countDoneSubtasks = 0;
        int countNewSubtasks = 0;

        if (subtasks.isEmpty()) {
            return status;
        }

        for (int item : subtasks) {
            Status subtaskStatus = getTaskToId(item).getStatus();
            if (subtaskStatus == Status.NEW) {
                countNewSubtasks++;
            }
            if (subtaskStatus == Status.DONE) {
                countDoneSubtasks++;
            }
        }

        if (countDoneSubtasks == subtasks.size()) {
            status = Status.DONE;
        } else if (countNewSubtasks == subtasks.size()) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
        return status;
    }
}
