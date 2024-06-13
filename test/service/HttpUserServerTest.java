package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpUserServer;
import user.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpUserServerTest {

    private HttpUserServer userServer;
    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private UserManager userManager;
    private Task task;
    private User user;

    @BeforeEach
    void init() throws IOException {
        userManager = Managers.getDefaultUser();
        taskManager = userManager.getTaskManager();
        userServer = new HttpUserServer(userManager);
        user = new User("Иванов Иван Иванович");
        userManager.add(user);
        task = new Task("Test task", "Test task description", Task.Status.NEW, Duration.ofMinutes(15), LocalDateTime.now(), user);
        taskManager.createTask(task);
        System.out.println(task.getUser());
        System.out.println(task.toString());
        userServer.start();
    }

    @AfterEach
    void tearDown() {
        userServer.stop();
    }

    @Test
    void getUsers() throws IOException, InterruptedException, URISyntaxException {
        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        // Send request and get response
        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, stringBodyHandler);

        assertEquals(200, response.statusCode());
        Type userType = new TypeToken<ArrayList<User>>() {
        }.getType();
        List<User> actual = gson.fromJson(response.body(), userType);

        // Verify the response
        assertNotNull(actual, "Пользователи не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество пользователей");
        assertEquals(user, actual.get(0), "Пользователи не совпадают");
    }

    @Test
    void getUserById() throws IOException, InterruptedException {
        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type userType = new TypeToken<User>() {
        }.getType();
        User actual = gson.fromJson(response.body(), userType);

        // Verify the response
        assertNotNull(actual, "Пользователи не возвращаются");
        assertEquals(user, actual, "Пользователи не совпадают");
    }

    @Test
    void deleteUser() throws IOException, InterruptedException {
        // Create HTTP client and request
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response
        assertEquals(200, response.statusCode());
    }
}
