package model;

public class Subtask extends Task {

    Epic epic;

    public Subtask(Long id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status);
        super.setType(Type.SUBTASK);
        this.epic = epic;
    }

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        super.setType(Type.SUBTASK);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
