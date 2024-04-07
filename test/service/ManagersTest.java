package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void getDefaultCheckResult() {
        // setup
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Managers managers = new Managers();
        Managers.setTaskManager(taskManager);

        // act
        TaskManager result = managers.getDefault();

        // verify
        assertEquals(result, taskManager);
    }

    @Test
    public void getDefaultInMemoryTaskManagerIsNullCheckResult() {
        // setup
        Managers managers = new Managers();
        Managers.setTaskManager(null);

        // act
        TaskManager result = managers.getDefault();

        // verify
        assertNotNull(result);
    }

    @Test
    public void getDefaultHistoryCheckResult() {
        // setup
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Managers managers = new Managers();
        Managers.setHistoryManager(historyManager);

        // act
        HistoryManager result = managers.getDefaultHistory();

        // verify
        assertEquals(result, historyManager);
    }

    @Test
    public void getDefaultHistoryInMemoryHistoryManagerIsNullCheckResult() {
        // setup
        Managers managers = new Managers();
        Managers.setHistoryManager(null);

        // act
        HistoryManager result = managers.getDefaultHistory();

        // verify
        assertNotNull(result);
    }

}