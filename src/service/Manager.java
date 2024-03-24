package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private Long id = 0L;
    private HashMap<Long, Task> tasks = new HashMap<>();

    public Long getNextId() {
        return id++;
    }

    public Long createTask(String name, String description) {
        Long currentId = getNextId();
        Task task = new Task(currentId, name, description, Task.Status.NEW);
        tasks.put(currentId, task);
        return currentId;
    }

    public Long createTask(Task task) {
        if (task.getId() == null) {
            task.setId(getNextId());
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Task getById(Long id) {
        return tasks.get(id);
    }

    public void delete(Long id) {
        tasks.remove(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public List<Task> getAllTasks() {
        ArrayList<Task> tasks1 = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (!(value instanceof Epic || value instanceof Subtask)) {
                tasks1.add(value);
            }
        }
        return tasks1;
    }

    public void deleteAllTasks() {
        List<Task> all = getAllTasks();
        for (Task task : all) {
            tasks.remove(task.getId());
        }
    }

    public Long createEpic(String name, String description) {
        Long currentId = getNextId();
        Epic epic = new Epic(currentId, name, description, Task.Status.NEW);
        tasks.put(currentId, epic);
        return currentId;
    }

    public Long createEpic(Epic epic) {
        if (epic.getId() == null) {
            epic.setId(getNextId());
        }
        tasks.put(epic.getId(), epic);
        return epic.getId();
    }

    public List<Epic> getAllEpic() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (value instanceof Epic) {
                epics.add((Epic) value);
            }
        }
        return epics;
    }

    public void deleteAllEpic() {
        List<Epic> all = getAllEpic();
        deleteAllSubtask();
        for (Task task : all) {
            tasks.remove(task.getId());
        }
    }

    public void clearAllEpicSubtask() {
        List<Epic> all = getAllEpic();
        for (Epic task : all) {
            task.getSubtasks().clear();
        }
    }

    public Epic getEpicById(Long id) {
        return (Epic) tasks.get(id);
    }

    public void updateEpic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    public void deleteEpicById(Long id) {
        ArrayList<Subtask> subtasks = getSubtaskByEpicId((tasks.get(id)).getId());
        for (Subtask subtask : subtasks) {
            tasks.remove(subtask.getId());
        }
        tasks.remove(id);
    }

    public ArrayList<Subtask> getSubtaskByEpicId(Long id) {
        Epic epic = (Epic) tasks.get(id);
        return epic.getSubtasks();
    }

    public Long createSubtask(String name, String description, Epic epic) {
        Long currentId = getNextId();
        Subtask subtask = new Subtask(currentId, name, description, Task.Status.NEW, epic);
        tasks.put(currentId, subtask);
        return currentId;
    }

    public Long createSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            subtask.setId(getNextId());
        }
        tasks.put(subtask.getId(), subtask);
        subtask.getEpic().getSubtasks().add(subtask);
        return subtask.getId();
    }

    public List<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (value instanceof Subtask) {
                subtasks.add((Subtask) value);
            }
        }
        return subtasks;
    }

    public void deleteAllSubtask() {
        List<Subtask> all = getAllSubtask();
        clearAllEpicSubtask();
        for (Task task : all) {
            tasks.remove(task.getId());
        }
    }

    public void deleteSubtaskId(Long id) {
        ArrayList<Subtask> subtasks = ((Subtask) tasks.get(id)).getEpic().getSubtasks();
        subtasks.remove((Subtask) tasks.get(id));
        tasks.remove(id);
    }

    public Subtask getSubtaskById(Long id) {
        return (Subtask) tasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = (Subtask) tasks.get(subtask.getId());//эпик
        deleteSubtaskId(subtask.getId());
        subtask.getEpic().getSubtasks().add(subtask);
        if (subtask.getStatus() != oldSubtask.getStatus()) {
            subtask.getEpic().updateStatus();
        }
        tasks.put(subtask.getId(), subtask);
    }
}
