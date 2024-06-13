package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    Managers managers = new Managers();
    TaskManager manager = managers.getDefaultFile();
    Gson gson = Managers.getGson();
    TaskHandler taskHandler = new TaskHandler();

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
                response = taskHandler.handleDeleteRequest(httpExchange);
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
        String response = "";
        String[] splitStrings = path.split("/");
        if (splitStrings.length == 2) {
            List<Subtask> allSubtask = manager.getAllSubtask();
            try {
                response = gson.toJson(allSubtask);
            } catch (Exception e) {
                System.out.println(e);
            }
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
        Subtask subtask = Managers.getGson().fromJson(taskJson, Subtask.class);
        if (subtask.getEpicID() != null) {
            Epic epic = (Epic) manager.taskById(subtask.getEpicID());
            subtask.setEpic(epic);
            subtask.setEpicID(null);
        }
        Long taskId = manager.createSubtask(subtask);
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
