package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Long id = 0L;
    private HashMap<Long, Task> tasks = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();

    public Long getNextId() {
        return id++;
    }

    @Override
    public Long createTask(String name, String description) {
        Long currentId = getNextId();
        Task task = new Task(currentId, name, description, Task.Status.NEW);
        tasks.put(currentId, task);
        return currentId;
    }

    @Override
    public Long createTask(Task task) {
        if (task.getId() == null) {
            task.setId(getNextId());
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Task taskById(Long id) {
        Task task = tasks.get(id);
        historyManager.add(new Task(task));
        return task;
    }

    @Override
    public void delete(Long id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public List<Task> getAllTasks() {
        ArrayList<Task> tasks1 = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (!(value instanceof Epic || value instanceof Subtask)) {
                tasks1.add(value);
            }
        }
        return tasks1;
    }

    @Override
    public void deleteAllTasks() {
        List<Task> all = getAllTasks();
        for (Task task : all) {
            delete(task.getId());
        }
    }

    //Epic
    @Override
    public Long createEpic(String name, String description) {
        Long currentId = getNextId();
        Epic epic = new Epic(currentId, name, description, Task.Status.NEW);
        tasks.put(currentId, epic);
        return currentId;
    }

    @Override
    public Long createEpic(Epic epic) {
        if (epic.getId() == null) {
            epic.setId(getNextId());
        }
        tasks.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public List<Epic> getAllEpic() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (value instanceof Epic) {
                epics.add((Epic) value);
            }
        }
        return epics;
    }

    @Override
    public void deleteAllEpic() {
        List<Epic> all = getAllEpic();
        deleteAllSubtask();
        for (Task task : all) {
            delete(task.getId());
        }
    }

    @Override
    public void clearAllEpicSubtask() {
        List<Epic> all = getAllEpic();
        for (Epic task : all) {
            task.getSubtasks().clear();
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(Long id) {
        List<Long> subtasks = getSubtaskByEpicId((tasks.get(id)).getId()).stream().map(Task::getId).toList();
        for (Long subtask : subtasks) {
            deleteSubtaskId(subtask);
        }
        delete(id);
    }

    //Subtask
    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(Long id) {
        Epic epic = (Epic) tasks.get(id);
        return epic.getSubtasks();
    }

    @Override
    public Long createSubtask(String name, String description, Epic epic) {
        Long currentId = getNextId();
        Subtask subtask = new Subtask(currentId, name, description, Task.Status.NEW, epic);
        tasks.put(currentId, subtask);
        return currentId;
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            subtask.setId(getNextId());
        }
        tasks.put(subtask.getId(), subtask);
        subtask.getEpic().getSubtasks().add(subtask);
        return subtask.getId();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Task value : tasks.values()) {
            if (value instanceof Subtask) {
                subtasks.add((Subtask) value);
            }
        }
        return subtasks;
    }

    @Override
    public void deleteAllSubtask() {
        List<Subtask> all = getAllSubtask();
        clearAllEpicSubtask();
        for (Task task : all) {
            delete(task.getId());
        }
    }

    @Override
    public void deleteSubtaskId(Long id) {
        ArrayList<Subtask> subtasks = ((Subtask) tasks.get(id)).getEpic().getSubtasks();
        subtasks.remove((Subtask) tasks.get(id));
        delete(id);
    }

    @Override
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
