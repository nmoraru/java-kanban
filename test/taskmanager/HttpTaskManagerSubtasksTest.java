package taskmanager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.data.Epic;
import taskmanager.data.Subtask;
import taskmanager.data.Task;
import taskmanager.manager.InMemoryTaskManager;
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

public class HttpTaskManagerSubtasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerSubtasksTest() throws IOException {
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
    public void testAddSubtask() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task = new Subtask("Test2", "Testing2", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        manager.createEpic(epic);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        Collection tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(task), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtaskInBusyTime() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Task task = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.01.1990 11:00");
        Subtask subtask = new Subtask("Test1", "Testing1", 3,
                NEW, 1000, "11.01.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createTask(task);
        String taskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
        assertEquals("Subtask not create. Is busy time for subtask.", response.body(), "Некорректное сообщение");
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task1 = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        Subtask task2 = new Subtask("Test2", "Testing2", 3,
                NEW, 1000, "11.02.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createSubtask(task1);
        manager.createSubtask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Collection tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(task1), "Некорректное имя задачи");
        assertEquals(true, tasksFromManager.contains(task2), "Некорректное имя задачи");
    }

    @Test
    public void testGetExistSubtaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task1 = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createSubtask(task1);
        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
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
    public void testGetNotExistSubtasksToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task1 = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createTask(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");
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
    public void testDeleteExistSubtaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task1 = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        Subtask task2 = new Subtask("Test2", "Testing2", 3,
                NEW, 1000, "11.02.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createSubtask(task1);
        manager.createSubtask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        Collection tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(task2), "Задача не найдена");
    }

    @Test
    public void testDeleteNotExistSubtaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask task1 = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        Subtask task2 = new Subtask("Test2", "Testing2", 3,
                NEW, 1000, "11.02.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createSubtask(task1);
        manager.createSubtask(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/11");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(404, response.statusCode());

        Collection tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(task2), "Задача не найдена");
        assertTrue(tasksFromManager.contains(task1), "Задача не найдена");
    }

    @Test
    public void testUpdateSubtaskToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        Subtask subtask = new Subtask("Test1", "Testing1", 2,
                NEW, 1000, "11.01.1990 11:00", 1);
        Subtask subtaskUPD = new Subtask("Test2", "Testing2", 2,
                DONE, 1000, "11.02.1990 11:00", 1);
        // конвертируем её в JSON
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        String taskJson = gson.toJson(subtaskUPD);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        assertEquals(subtaskUPD.getStatus(), manager.getSubtaskToId(2).getStatus(), "Задача не найдена");
    }
}
