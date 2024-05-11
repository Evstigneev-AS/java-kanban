package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void getDefaultCheckResult() {
        // setup - object with which we will work and the input data
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Managers managers = new Managers();
        Managers.setTaskManager(taskManager);

        // act - perform the operation we are testing
        TaskManager result = managers.getDefault();

        // verify - statements about the correctness of an action
        assertEquals(result, taskManager);
    }

    @Test
    public void getDefaultInMemoryTaskManagerIsNullCheckResult() {
        // setup - object with which we will work and the input data
        Managers managers = new Managers();
        Managers.setTaskManager(null);

        // act - perform the operation we are testing
        TaskManager result = managers.getDefault();

        // verify - statements about the correctness of an action
        assertNotNull(result);
    }

    @Test
    public void getDefaultHistoryCheckResult() {
        // setup - object with which we will work and the input data
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Managers managers = new Managers();
        Managers.setHistoryManager(historyManager);

        // act - perform the operation we are testing
        HistoryManager result = managers.getDefaultHistory();

        // verify - statements about the correctness of an action
        assertEquals(result, historyManager);
    }

    @Test
    public void getDefaultHistoryInMemoryHistoryManagerIsNullCheckResult() {
        // setup - object with which we will work and the input data
        Managers managers = new Managers();
        Managers.setHistoryManager(null);

        // act - perform the operation we are testing
        HistoryManager result = managers.getDefaultHistory();

        // verify - statements about the correctness of an action
        assertNotNull(result);
    }

}