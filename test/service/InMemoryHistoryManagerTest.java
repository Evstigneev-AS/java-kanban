package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.Task.Status.IN_PROGRESS;
import static model.Task.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    @Test
    public void tasksAddedToHistoryManagerRetainPreviousVersionOfTask() {
        // setup - object with which we will work and the input data
        TaskManager taskManager = Managers.getDefault();
        HistoryManager defaultHistory = Managers.getDefaultHistory();
        Long task1 = taskManager.createTask(new Task("Test addNewTask", "Test addNewTask description", NEW));
        Task byId = taskManager.taskById(task1);
        byId.setStatus(IN_PROGRESS);
        taskManager.updateTask(byId);

        // act - perform the operation we are testing
        Task historyTask = defaultHistory.getHistory().getFirst();

        // verify - statements about the correctness of an action
        assertEquals(NEW, historyTask.getStatus());

    }

    @Test
    void addNewHistoryOnListHistoryCheckListHistory() {
        // setup - object with which we will work and the input data
        Managers.setHistoryManager(null);
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTaskHistory", "Test addNewTask description", NEW);

        // act - perform the operation we are testing
        Long taskId = taskManager.createTask(task);
        taskManager.taskById(taskId);

        // verify - statements about the correctness of an action
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}