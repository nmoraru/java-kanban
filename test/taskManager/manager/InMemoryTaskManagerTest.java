package taskManager.manager;

import org.junit.jupiter.api.Test;
import taskManager.data.Epic;
import taskManager.data.Status;
import taskManager.data.Subtask;
import taskManager.data.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public void shouldBeCreateTask() {
        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);

        tm.createTask(task1);

        assertEquals(task1, tm.getTaskToId(1));
    }

    @Test
    public void shouldBeCreateEpic() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);

        tm.createEpic(epic1);

        assertEquals(epic1, tm.getEpicToId(1));
    }

    @Test
    public void shouldBeCreateSubtask() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.NEW, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);

        assertEquals(subtask1, tm.getSubtaskToId(2));
    }

    @Test
    public void shouldBeGenerateNewIdWhenCreateNewTask() {
        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 1, Status.NEW, 2);

        tm.createTask(task1);
        tm.createEpic(epic1);
        tm.createSubtask(subtask1);

        assertEquals(task1, tm.getTaskToId(1));
        assertEquals(epic1, tm.getEpicToId(2));
        assertEquals(subtask1, tm.getSubtaskToId(3));
    }

    @Test
    public void shouldBeConsistTaskFieldsAfterCreated() {
        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);

        tm.createTask(task1);
        Task actualTask = tm.getTaskToId(1);

        assertEquals(task1.getName(), actualTask.getName());
        assertEquals(task1.getDescription(), actualTask.getDescription());
        assertEquals(task1.getStatus(), actualTask.getStatus());
    }

    @Test
    public void shouldBeConsistEpicFieldsAfterCreated() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);

        tm.createEpic(epic1);
        Epic actualEpic = tm.getEpicToId(1);

        assertEquals(epic1.getName(), actualEpic.getName());
        assertEquals(epic1.getDescription(), actualEpic.getDescription());
        assertEquals(epic1.getStatus(), actualEpic.getStatus());
    }

    @Test
    public void shouldBeConsistSubtaskFieldsAfterCreated() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.NEW, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);
        Subtask actualSubtask = tm.getSubtaskToId(2);

        assertEquals(subtask1.getName(), actualSubtask.getName());
        assertEquals(subtask1.getDescription(), actualSubtask.getDescription());
        assertEquals(subtask1.getStatus(), actualSubtask.getStatus());
        assertEquals(subtask1.getEpicId(), actualSubtask.getEpicId());
    }
}