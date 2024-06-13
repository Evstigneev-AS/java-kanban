package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handler.HistoryHandler;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryHandlerTest {
    private HttpServer server;
    private HistoryManager historyManager;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        historyManager = Managers.getDefaultHistory();
        gson = Managers.getGson();

        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/history", new HistoryHandler());
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void testHandleGetRequest() throws IOException, InterruptedException {
        // Create and add tasks to history
        Task task1 = new Task(1L, "Task 1", "Description 1", Task.Status.NEW);
        Task task2 = new Task(2L, "Task 2", "Description 2", Task.Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response
        assertEquals(200, response.statusCode());
        List<Task> tasksFromResponse = gson.fromJson(response.body(), List.class);
        assertNotNull(tasksFromResponse, "Tasks not returned");
        assertEquals(2, tasksFromResponse.size(), "Incorrect number of tasks returned");
    }

    @Test
    public void testHandleInvalidRequestMethod() throws IOException, InterruptedException {
        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.noBody()).build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response
        assertEquals(500, response.statusCode());
    }

    @Test
    public void testHandleInvalidPath() throws IOException, InterruptedException {
        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/history/extra");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response
        assertEquals(500, response.statusCode());
    }
}