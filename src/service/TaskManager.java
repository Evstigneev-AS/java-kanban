package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Subtask> getAllSubtask();

    List<Epic> getAllEpic();

    List<Task> getAllTasks();

    Long createTask(String name, String description);

    Long createTask(Task task);

    Task taskById(Long id);

    void delete(Long id);

    void updateTask(Task task);

    void deleteAllTasks();

    Long createEpic(String name, String description);

    Long createEpic(Epic epic);

    void deleteAllEpic();

    void clearAllEpicSubtask();

    void updateEpic(Epic epic);

    void deleteEpicById(Long id);

    ArrayList<Subtask> getSubtaskByEpicId(Long id);

    Long createSubtask(String name, String description, Epic epic);

    Long createSubtask(Subtask subtask);

    void deleteAllSubtask();

    void deleteSubtaskId(Long id);

    void updateSubtask(Subtask subtask);

    List<Task> getPrioritizedTasks();
}
