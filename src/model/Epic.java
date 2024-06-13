package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private transient ArrayList<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(Long id, String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(id, name, description, status);
        super.setType(Type.EPIC);
        this.subtasks = subtasks;
    }

    public Epic(String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(name, description, status);
        super.setType(Type.EPIC);
        this.subtasks = subtasks;
    }

    public Epic(Long id, String name, String description, Status status) {
        super(id, name, description, status);
        super.setType(Type.EPIC);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        super.setType(Type.EPIC);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateStatus() {
        int subtaskTrueNew = 0;
        int subtaskTrueDone = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.NEW) {
                subtaskTrueNew++;
                if (subtasks.size() == subtaskTrueNew) {
                    setStatus(Status.NEW);
                }
            }
            if (subtask.getStatus() == Status.DONE) {
                subtaskTrueDone++;
                if (subtasks.size() == subtaskTrueDone) {
                    setStatus(Status.DONE);
                }
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void calculateDurationAndTimes() {
        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() == null || subtask.getDuration() == null || subtask.getEndTime() == null) {
                continue;
            }
            if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                earliestStart = subtask.getStartTime();
            }
            if (latestEnd == null || subtask.getEndTime().isAfter(latestEnd)) {
                latestEnd = subtask.getEndTime();
            }
        }
        setStartTime(earliestStart);
        this.endTime = latestEnd;
        if (earliestStart != null) {
            totalDuration = Duration.between(earliestStart, this.endTime);
        }
        setDuration(totalDuration);
    }


    @Override
    public LocalDateTime getEndTime() {
        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null) {
                if (subtask.getEndTime().isAfter(endTime)) {
                    this.endTime = subtask.getEndTime();
                }
            } else {
                return this.endTime = null;
            }
        }
        return this.endTime;
    }
}
