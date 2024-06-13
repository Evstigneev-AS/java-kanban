package server;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
    }

    public static void main(String[] args) throws IOException {
        Managers managers = new Managers();
        TaskManager manager = managers.getDefault();
        HttpTaskServer httpServer = new HttpTaskServer(manager);
        httpServer.start();
        httpServer.stop();
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:8080/tasks/");
        System.out.println("http://localhost:8080/subtasks/");
        System.out.println("http://localhost:8080/epics/");
        System.out.println("http://localhost:8080/history/");
        System.out.println("http://localhost:8080/prioritized/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }
}
