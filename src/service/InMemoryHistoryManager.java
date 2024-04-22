package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        boolean taskDouble = this.history.contains(task);
        if (taskDouble) {
            this.history.remove(task);
        }
        this.history.add(task);
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(this.history);
    }

}
