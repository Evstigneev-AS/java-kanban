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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {
    Managers managers = new Managers();
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = managers.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = managers.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskId() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1L, "Test 2", "Testing task 2",
                Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson1 = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();


        // десериализуем json -> task
        Task task1 = Managers.getGson().fromJson(taskJson1, Task.class);
        Long taskId = manager.createTask(task1);
        Task createTask = manager.taskById(taskId);
        String taskJson2 = gson.toJson(createTask);

        // проверяем код ответа
        assertEquals(taskJson1, taskJson2, "Некорректная задача");
    }

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1L, "Test 2", "Testing task 2",
                Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson1 = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();


        // десериализуем json -> task
        Task task1 = Managers.getGson().fromJson(taskJson1, Task.class);
        Long taskId = manager.createTask(task1);
        Task createTask = manager.taskById(taskId);
        String taskJson2 = gson.toJson(createTask);

        // проверяем код ответа
        assertNotNull(createTask, "Некорректная задача");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1L, "Test 2", "Testing task 2",
                Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        //создаем изменённую задачу
        Task taskUpdate = new Task(1L, "Test 2", "Testing task 2",
                Task.Status.DONE, Duration.ofDays(10), LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson1 = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();

        // десериализуем json -> task
        Task task1 = Managers.getGson().fromJson(taskJson1, Task.class);
        Long taskId = manager.createTask(task1);
        manager.updateTask(taskUpdate);

        // проверяем код ответа
        assertNotEquals(manager.taskById(taskId), task);
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1L, "Test 2", "Testing task 2",
                Task.Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        // конвертируем её в JSON
        String taskJson1 = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();

        // десериализуем json -> task
        Task task1 = Managers.getGson().fromJson(taskJson1, Task.class);
        Long taskId = manager.createTask(task1);
        Long deleteId = taskId;
        manager.delete(deleteId);
        List<Task> tasks = new ArrayList<>();

        // проверяем код ответа
        assertEquals(tasks, manager.getAllTasks(), "в списке есть задача");
    }
}