package taskmanager.data;

public class Subtask extends Task {
    public int epicId;

    public Subtask(String name, String description, int id, Status status, long duration, String startTime, int epic) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epic;
        type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic='" + epicId + '\'' +
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
}
