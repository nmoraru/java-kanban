package taskmanager.httptaskserver;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.HttpTaskServer;
import taskmanager.data.Task;
import taskmanager.manager.InMemoryTaskManager;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static taskmanager.data.Status.NEW;

public class HttpTaskManagerPrioritizedTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    public HttpTaskManagerPrioritizedTasksTest() throws IOException {
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
    public void testPrioritizedTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test1", "Testing1", 1,
                NEW, 1000, "11.01.1990 11:00");
        Task task2 = new Task("Test2", "Testing2", 2,
                NEW, 1000, "11.02.1990 11:00");
        Task task3 = new Task("Test3", "Testing3", 3,
                NEW, 1000, "01.02.1990 11:00");
        // конвертируем её в JSON
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        String expectedResult = "[\n" +
                "  {\n" +
                "    \"name\": \"Test1\",\n" +
                "    \"type\": \"TASK\",\n" +
                "    \"description\": \"Testing1\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"duration\": 1000,\n" +
                "    \"startTime\": \"11.01.1990 11:00\",\n" +
                "    \"endTime\": \"12.01.1990 03:40\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Test3\",\n" +
                "    \"type\": \"TASK\",\n" +
                "    \"description\": \"Testing3\",\n" +
                "    \"id\": 3,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"duration\": 1000,\n" +
                "    \"startTime\": \"01.02.1990 11:00\",\n" +
                "    \"endTime\": \"02.02.1990 03:40\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Test2\",\n" +
                "    \"type\": \"TASK\",\n" +
                "    \"description\": \"Testing2\",\n" +
                "    \"id\": 2,\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"duration\": 1000,\n" +
                "    \"startTime\": \"11.02.1990 11:00\",\n" +
                "    \"endTime\": \"12.02.1990 03:40\"\n" +
                "  }\n" +
                "]";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertEquals(expectedResult, response.body(), "Некорректное количество задач");
    }
}
