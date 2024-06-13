package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Long id = 0L;
    protected HashMap<Long, Task> table = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> prioritizedTasks;

    public Long getNextId() {
        return id++;
    }

    @Override
    public Long createTask(String name, String description) {
        Long currentId = getNextId();
        Task task = new Task(currentId, name, description, Task.Status.NEW);
        table.put(currentId, task);
        return currentId;
    }

    @Override
    public Long createTask(Task task) {
        if (!validationTask(task)) {
            return null;
        }
        if (task.getId() == null) {
            task.setId(getNextId());
        }
        table.put(task.getId(), task);
        return task.getId();
    }

    public Task taskById(Long id) {
        Task task = table.get(id);
        historyManager.add(new Task(task));
        return task;
    }

    @Override
    public void delete(Long id) {
        table.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        if (!validationTask(task)) {
            return;
        }
        if (task.getId() == null) {
            task.setId(getNextId());
        }
        table.put(task.getId(), task);
    }

    @Override
    public List<Task> getAllTasks() {
        ArrayList<Task> tasks1 = new ArrayList<>();
        for (Task value : table.values()) {
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
        table.put(currentId, epic);
        return currentId;
    }

    @Override
    public Long createEpic(Epic epic) {
        if (epic.getId() == null) {
            epic.setId(getNextId());
        }
        table.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public List<Epic> getAllEpic() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task value : table.values()) {
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
        table.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(Long id) {
        List<Long> subtasks = getSubtaskByEpicId((table.get(id)).getId()).stream().map(Task::getId).toList();
        for (Long subtask : subtasks) {
            deleteSubtaskId(subtask);
        }
        delete(id);
    }

    //Subtask
    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(Long id) {
        Epic epic = (Epic) table.get(id);
        return epic.getSubtasks();
    }

    @Override
    public Long createSubtask(String name, String description, Epic epic) {
        Long currentId = getNextId();
        Subtask subtask = new Subtask(currentId, name, description, Task.Status.NEW, epic);
        table.put(currentId, subtask);
        return currentId;
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            subtask.setId(getNextId());
        }
        table.put(subtask.getId(), subtask);
        subtask.getEpic().getSubtasks().add(subtask);
        subtask.getEpic().calculateDurationAndTimes();
        return subtask.getId();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Task value : table.values()) {
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
        ArrayList<Subtask> subtasks = ((Subtask) table.get(id)).getEpic().getSubtasks();
        subtasks.remove((Subtask) table.get(id));
        Epic epic = ((Subtask) table.get(id)).getEpic();
        delete(id);
        epic.calculateDurationAndTimes();

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = (Subtask) table.get(subtask.getId());//эпик
        deleteSubtaskId(subtask.getId());
        subtask.getEpic().getSubtasks().add(subtask);
        if (subtask.getStatus() != oldSubtask.getStatus()) {
            subtask.getEpic().updateStatus();
        }
        table.put(subtask.getId(), subtask);
        subtask.getEpic().calculateDurationAndTimes();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        for (Task task : table.values()) {
            addPrioritizedTasks(task);
        }
        return new ArrayList<>(prioritizedTasks);
    }

    public void addPrioritizedTasks(Task task) {
        for (Task existingTask : prioritizedTasks) {
            if (areTasksOverlapping(existingTask, task)) {
                return;
            }
        }
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    public void addTask(Task task) {

    }

    public boolean validationTask(Task task) {
        for (Task addTask : table.values()) {
            if (addTask.getId().equals(task.getId())) {
                continue;
            }
            if (areTasksOverlapping(addTask, task)) {
                return false;
            }
        }
        return true;
    }

    public boolean areTasksOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();
        if (end1.equals(end2)) {
            return true;
        }
        if (start2.equals(start1)) {
            return true;
        }
        if (start2.isAfter(start1) && start2.isBefore(end1)) {
            return true;
        }
        if (end2.isBefore(end1) && end2.isAfter(start1)) {
            return true;
        }
        return false;
    }
}
