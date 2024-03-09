package taskManager.data;

import java.util.TreeSet;

public class Epic extends Task {
    private TreeSet<Integer> subtasksId;

    public Epic(String name, String description, int id, Status status, TreeSet<Integer> subtasksId) {
        super(name, description, id, status);
        if (subtasksId == null) {
            subtasksId = new TreeSet<>();
        }
        this.subtasksId = subtasksId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasksId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public TreeSet<Integer> getSubtasksId() {
        return subtasksId;
    }
}
