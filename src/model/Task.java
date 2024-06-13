package model;

import user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Type type;
    private Duration duration;
    private LocalDateTime startTime;
    private User user;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm");

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public Task(Long id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task(Long id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Long id, String name, String description, Status status, Duration duration, LocalDateTime startTime, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
        this.user = user;
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime, User user) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
        this.user = user;
    }

    public Task(Task t) {
        this(t.getId(), t.getName(), t.getDescription(), t.getStatus(), t.getDuration(), t.getStartTime(), t.getUser());
    }

    @Override
    public String toString() {
        if (startTime != null && duration != null) {
            return "Task{" +
                    "id=" + id +
                    ", type=" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status + '\'' +
                    ", startTime='" + startTime.format(formatter) + '\'' +
                    ", endTime=" + getEndTime().format(formatter) +
                    '}';
        } else {
            return "Task{" +
                    "id=" + id +
                    ", type=" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
