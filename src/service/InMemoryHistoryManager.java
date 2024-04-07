package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (this.history.size() == 10) {
            this.history.removeFirst();
        }
        this.history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        history.addAll(this.history);
        return history;
    }

}
