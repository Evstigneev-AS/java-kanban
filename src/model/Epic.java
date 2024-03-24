package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(Long id, String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    public Epic(String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(name, description, status);
        this.subtasks = subtasks;
    }

    public Epic(Long id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
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
}
