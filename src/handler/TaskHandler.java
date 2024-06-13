package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.FileBackedTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    Managers managers = new Managers();
    TaskManager manager = managers.getDefaultFile();
    Gson gson = Managers.getGson();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String name = splitStrings[1];
        System.out.println("Началась обработка " + requestMethod + " /" + name + " запроса от клиента.");
        String response;
        switch (requestMethod) {
            case "GET":
                response = handleGetRequest(httpExchange);
                break;
            case "POST":
                response = handlePostRequest(httpExchange);
                break;
            case "DELETE":
                response = handleDeleteRequest(httpExchange);
                break;
            default:
                sendInternalServerError(httpExchange);//500
                return;
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetRequest(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String response = "";
        if (splitStrings.length == 2) {
            List<Task> allTasks = manager.getAllTasks();
            response = gson.toJson(allTasks);
            System.out.println(response);
        } else {
            long id = Long.parseLong(splitStrings[2]);
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
            if (fileBackedTaskManager.getTable().get(id) != null) {
                Task task = manager.taskById(id);
                System.out.println(task);
                try {
                    String json = gson.toJson(task);
                    System.out.println(json);
                    response = json;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                sendNotFound(httpExchange);//404
            }
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        return response;
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + taskJson);
        Task task = Managers.getGson().fromJson(taskJson, Task.class);
        Long id = task.getId();
        if (id != null) {
            manager.updateTask(task);
            Task taskCreate = manager.taskById(id);
            String json = Managers.getGson().toJson(taskCreate);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(json.getBytes());
                return json;
            }
        } else {
            Long taskId = manager.createTask(task);
            if (taskId == null) {
                sendHasInteractions(httpExchange);//406
            }
            Task taskCreate = manager.taskById(taskId);
            String json = Managers.getGson().toJson(taskCreate);
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(json.getBytes());
                return json;
            }
        }
    }

    protected String handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String response = "";
        String[] splitStrings = path.split("/");
        if (splitStrings.length == 3 && splitStrings[1].equals("tasks")) {
            String pathId = splitStrings[2];
            long id = Long.parseLong(pathId);
            try {
                manager.delete(id);
            } catch (IndexOutOfBoundsException e) {
            }
            System.out.println("Удалили task id = " + id);
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        return response;
    }
}
