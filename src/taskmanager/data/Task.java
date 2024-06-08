package taskmanager.data;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected Type type;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, int id, Status status, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        type = Type.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime.equals("null") ? null : LocalDateTime.parse(startTime, formatter);
        endTime = startTime.equals("null") ? null : LocalDateTime.parse(startTime, formatter).plus(Duration.ofMinutes(duration));
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        type = Type.TASK;
        this.duration = null;
        this.startTime = null;
        endTime = null;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }

    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + (startTime != null ? startTime.format(formatter) : null) +
                ", endTime=" + (endTime != null ? endTime.format(formatter) : null) +
                ", duration="  + (duration != null ? duration.toMinutes() : null) +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return Timestamp.valueOf(this.startTime).compareTo(Timestamp.valueOf(o.startTime));
    }
}
