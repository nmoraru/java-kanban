package taskmanager.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private static String defaultStartTime = "01.01.9999 00:00";
    private static String defaultEndTime = "01.01.0001 00:00";
    private static long defaultDuration = 0;
    private ArrayList<Subtask> subtasksInEpic = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW, defaultDuration, defaultStartTime);
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
                ", startTime=" + startTime.format(formatter) +
                ", endTime=" + endTime.format(formatter) +
                ", duration=" + duration.toMinutes() +
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
        calculateEpicDuration();
        calculateEpicStartTime();
        calculateEpicEndTime();
    }

    public void removeSubtaskToEpic(Subtask subtask) {
        subtasksInEpic.remove(subtask);
        calculateEpicStatus();
        calculateEpicDuration();
        calculateEpicStartTime();
        calculateEpicEndTime();
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
        startTime = LocalDateTime.parse(defaultStartTime, formatter);
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
        endTime = LocalDateTime.parse(defaultEndTime, formatter);
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
