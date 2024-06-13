package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
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
            List<Task> allTasks = manager.getPrioritizedTasks();
            response = gson.toJson(allTasks);
            System.out.println(response);
        } else {
            sendInternalServerError(httpExchange);//500
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        return response;
    }
}
