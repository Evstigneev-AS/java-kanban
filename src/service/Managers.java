package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.DurationSerializer;
import handler.LocalDateTimeSerializer;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private static HistoryManager historyManager;
    private static TaskManager taskManager;
    private static UserManager userManager;

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

    public static UserManager getDefaultUser() {
        return new InMemoryUserManager();
    }

    static void setHistoryManager(HistoryManager historyManager) {
        Managers.historyManager = historyManager;
    }

    static void setTaskManager(TaskManager taskManager) {
        Managers.taskManager = taskManager;
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        return gsonBuilder.create();
    }
}
