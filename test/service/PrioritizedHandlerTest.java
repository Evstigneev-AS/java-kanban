package service;

import com.google.gson.Gson;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrioritizedHandlerTest {
    Managers managers = new Managers();
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = managers.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = managers.getGson();

    public PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskServer.start();
        manager.deleteAllTasks();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
        manager.deleteAllTasks();
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        // Create and add tasks to the manager
        Task task1 = new Task("Task 1", "Description 1", Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Task.Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.createTask(task1);
        manager.createTask(task2);

        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
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
        URI uri = URI.create("http://localhost:8080/prioritized");
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
        URI uri = URI.create("http://localhost:8080/prioritized/extra");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response
        assertEquals(500, response.statusCode());
    }
}