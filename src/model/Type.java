package model;

public enum Type {
    TASK,
    EPIC,
    SUBTASK;


    public String toString(Type type) {
        return type.name();
    }

    public Type fromString(String value) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
