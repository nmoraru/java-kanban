package taskManager.data;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksInEpic;

    public Epic(String name, String description, int id, ArrayList<Subtask> subtasksInEpic) {
        super(name, description, id, Status.NEW);
        if (subtasksInEpic.isEmpty()) {
            subtasksInEpic = new ArrayList<>();
        }
        this.subtasksInEpic = subtasksInEpic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasksInEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public ArrayList<Subtask> getSubtasksInEpic() {
        return subtasksInEpic;
    }

    public void addSubtaskToEpic(Subtask subtask) {
        if (subtasksInEpic.contains(subtask)) {
            subtasksInEpic.remove(subtask);
        }
        subtasksInEpic.add(subtask);
        calculateEpicStatus();
    }

    public void removeSubtaskToEpic(Subtask subtask) {
        subtasksInEpic.remove(subtask);
        calculateEpicStatus();
    }

    public void calculateEpicStatus() {
        Status status = Status.NEW;
        int countDoneSubtasks = 0;
        int countNewSubtasks = 0;

        if (!subtasksInEpic.isEmpty()) {
            for (Subtask subtask : subtasksInEpic) {
                Status subtaskStatus = subtask.getStatus();
                if (subtaskStatus == Status.NEW) {
                    countNewSubtasks++;
                }
                if (subtaskStatus == Status.DONE) {
                    countDoneSubtasks++;
                }
            }

            if (countDoneSubtasks == subtasksInEpic.size()) {
                status = Status.DONE;
            } else if (countNewSubtasks == subtasksInEpic.size()) {
                status = Status.NEW;
            } else {
                status = Status.IN_PROGRESS;
            }
        }

        this.status = status;
    }
}
