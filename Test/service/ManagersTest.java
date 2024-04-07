package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void getDefault_checkResult() {
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
    public void getDefault_inMemoryTaskManagerIsNull_checkResult() {
        // setup
        Managers managers = new Managers();
        Managers.setTaskManager(null);

        // act
        TaskManager result = managers.getDefault();

        // verify
        assertNotNull(result);
    }

    @Test
    public void getDefaultHistory_checkResult() {
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
    public void getDefaultHistory_inMemoryHistoryManagerIsNull_checkResult() {
        // setup
        Managers managers = new Managers();
        Managers.setHistoryManager(null);

        // act
        HistoryManager result = managers.getDefaultHistory();

        // verify
        assertNotNull(result);
    }

}