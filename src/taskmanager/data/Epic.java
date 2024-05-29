package taskmanager.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final String MAX_TIME = "01.01.9999 00:00";
    private final String MIN_TIME = "01.01.0001 00:00";
    private ArrayList<Subtask> subtasksInEpic = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
        type = Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasksInEpic +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + (startTime != null ? startTime.format(formatter) : null) +
                ", endTime=" + (endTime != null ? endTime.format(formatter) : null) +
                ", duration="  + (duration != null ? duration.toMinutes() : null) +
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
        if (subtask.getStartTime() != null) {
            calculateEpicDuration();
            calculateEpicStartTime();
            calculateEpicEndTime();
        }
    }

    public void removeSubtaskToEpic(Subtask subtask) {
        subtasksInEpic.remove(subtask);
        calculateEpicStatus();
        if (startTime != null) {
            calculateEpicDuration();
            calculateEpicStartTime();
            calculateEpicEndTime();
        }
    }

    private void calculateEpicStatus() {
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

    private void calculateEpicDuration() {
        duration = Duration.ofMinutes(0);
        if (!subtasksInEpic.isEmpty()) {
            for (Subtask subtask : subtasksInEpic) {
                duration = duration.plus(subtask.duration);
            }
        }
    }

    private void calculateEpicStartTime() {
        startTime = LocalDateTime.parse(MAX_TIME, formatter);
        if (!subtasksInEpic.isEmpty()) {
            for (Subtask subtask : subtasksInEpic) {
                LocalDateTime subtaskStartTime = subtask.getStartTime();
                if (subtaskStartTime.isBefore(startTime)) {
                    startTime = subtaskStartTime;
                }
            }
        }
    }

    private void calculateEpicEndTime() {
        endTime = LocalDateTime.parse(MIN_TIME, formatter);
        if (!subtasksInEpic.isEmpty()) {
            for (Subtask subtask : subtasksInEpic) {
                LocalDateTime subtaskEndTime = subtask.getEndTime();
                if (subtaskEndTime.isAfter(endTime)) {
                    endTime = subtaskEndTime;
                }
            }
        }
    }
}
