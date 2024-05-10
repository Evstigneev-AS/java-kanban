package service;

public class Managers {
    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }

    public static TaskManager getDefaultFile() {
        if (taskManager == null) {
            taskManager = new FileBackedTaskManager();
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }

    static void setHistoryManager(HistoryManager historyManager) {
        Managers.historyManager = historyManager;
    }

    static void setTaskManager(TaskManager taskManager) {
        Managers.taskManager = taskManager;
    }
}
