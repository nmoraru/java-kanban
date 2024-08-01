package taskmanager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.data.Task;
import taskmanager.manager.InMemoryTaskManager;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static taskmanager.data.Status.DONE;
import static taskmanager.data.Status.NEW;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start(manager);
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test2", "Testing2", 1,
                NEW, 1000, "11.01.1990 11:00");
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        Collection tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(task), "Некорректное имя задачи");
    }

    @Test
    public void testAddTaskInBusyTime() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task2 = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.01.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        String taskJson = gson.toJson(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        Collection tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task not create. Is busy time for task.", response.body(), "Некорректное сообщение");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task2 = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.02.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        manager.createTask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Collection tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(task1), "Некорректное имя задачи");
        assertEquals(true, tasksFromManager.contains(task2), "Некорректное имя задачи");
    }

    @Test
    public void testGetExistTaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertEquals(taskJson, response.body(), "Задача не найдена");
    }

    @Test
    public void testGetNotExistTaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(404, response.statusCode());

        System.out.println(response.body());

        assertEquals("Invalid method.", response.body(), "Задача найдена");
    }

    @Test
    public void testDeleteExistTaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task2 = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.02.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        manager.createTask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        Collection tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(task2), "Задача не найдена");
    }

    @Test
    public void testDeleteNotExistTaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task2 = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.02.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        manager.createTask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/11");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(404, response.statusCode());

        Collection tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(task2), "Задача не найдена");
        assertTrue(tasksFromManager.contains(task1), "Задача не найдена");
    }

    @Test
    public void testUpdateTaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task1upd = new Task("Test1", "Testing1", 1,
                DONE, 2000, "11.01.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        String taskJson = gson.toJson(task1upd);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        assertEquals(task1upd.getStatus(), manager.getTaskToId(1).getStatus(), "Задача не найдена");
    }
}
