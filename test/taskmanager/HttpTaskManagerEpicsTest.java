package taskmanager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.data.Epic;
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

public class HttpTaskManagerEpicsTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    public HttpTaskManagerEpicsTest() throws IOException {
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
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test2", "Testing2", 1);
        // конвертируем её в JSON
        String taskJson = "{\n" +
                "  \"subtasksInEpic\": [],\n" +
                "  \"name\": \"Test2\",\n" +
                "  \"type\": \"EPIC\",\n" +
                "  \"description\": \"Testing2\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"NEW\"\n" +
                "}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        Collection tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(epic), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic("Test1", "Testing1", 1);
        Epic epic2 = new Epic("Test2", "Testing2", 2);
        // конвертируем её в JSON
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Collection tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(true, tasksFromManager.contains(epic1), "Некорректное имя задачи");
        assertEquals(true, tasksFromManager.contains(epic2), "Некорректное имя задачи");
    }

    @Test
    public void testGetExistEpicToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test1", "Testing1", 1);
        manager.createEpic(epic);
        String taskJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
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
    public void testGetNotExistEpicToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic("Test1", "Testing1", 1);
        // конвертируем её в JSON
        manager.createEpic(epic1);
        String taskJson = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
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
    public void testDeleteExistEpicToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic("Test1", "Testing1", 1);
        Epic epic2 = new Epic("Test2", "Testing2", 2);
        // конвертируем её в JSON
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        Collection tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(epic2), "Задача не найдена");
    }

    @Test
    public void testDeleteNotExistEpicToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic("Test1", "Testing1", 1);
        Epic epic2 = new Epic("Test2", "Testing2", 2);
        // конвертируем её в JSON
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/11");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(404, response.statusCode());

        Collection tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");

        assertTrue(tasksFromManager.contains(epic2), "Задача не найдена");
        assertTrue(tasksFromManager.contains(epic1), "Задача не найдена");
    }

    @Test
    public void testUpdateEpicToId() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic("Test1", "Testing1", 1);
        Epic epic1upd = new Epic("Test2", "Testing2", 1);
        // конвертируем её в JSON
        manager.createEpic(epic1);
        String taskJson = "{\n" +
                "  \"subtasksInEpic\": [],\n" +
                "  \"name\": \"Test2\",\n" +
                "  \"type\": \"EPIC\",\n" +
                "  \"description\": \"Testing2\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"NEW\"\n" +
                "}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        assertEquals(epic1upd.getStatus(), manager.getEpicToId(1).getStatus(), "Задача не найдена");
    }
}
