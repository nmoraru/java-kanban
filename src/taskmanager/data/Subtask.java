package taskmanager.data;

public class Subtask extends Task {
    public int epicId;

    public Subtask(String name, String description, int id, Status status, int epic) {
        super(name, description, id, status);
        this.epicId = epic;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic='" + epicId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
