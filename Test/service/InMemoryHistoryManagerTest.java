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
    public void tasks_added_to_HistoryManager_retain_previous_version_of_task() {
        // setup
        TaskManager taskManager = Managers.getDefault();
        HistoryManager defaultHistory = Managers.getDefaultHistory();
        Long task1 = taskManager.createTask(new Task("Test addNewTask", "Test addNewTask description", NEW));
        Task byId = taskManager.getById(task1);
        byId.setStatus(IN_PROGRESS);
        taskManager.updateTask(byId);

        // act
        Task historyTask = defaultHistory.getHistory().getFirst();

        // verify
        assertEquals(NEW, historyTask.getStatus());

    }

    @Test
    void addNewHistoryOnListHistory_checkListHistory() {
        // setup
        Managers.setHistoryManager(null);
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Test addNewTaskHistory", "Test addNewTask description", NEW);

        // act
        Long taskId = taskManager.createTask(task);
        taskManager.getById(taskId);

        // verify
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}