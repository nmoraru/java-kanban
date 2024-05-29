package taskmanager.manager;

import org.junit.jupiter.api.Test;
import taskmanager.data.Status;
import taskmanager.data.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    public void shouldNotBeSaveOldVersionTaskInHistory() {
        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task1_update = new Task("task1_update", "task1_update", 1, Status.DONE);

        tm.createTask(task1);
        tm.getTaskToId(1);
        tm.updateTask(task1_update);
        tm.getTaskToId(1);

        assertEquals(1, tm.getHistory().size());
        assertEquals(task1_update.getName(), tm.getHistory().get(0).getName());
        assertEquals(task1_update.getDescription(), tm.getHistory().get(0).getDescription());
        assertEquals(task1_update.getId(), tm.getHistory().get(0).getId());
        assertEquals(task1_update.getStatus(), tm.getHistory().get(0).getStatus());
    }

    @Test
    public void shouldBeEmptyHistory() {
        TaskManager tm = Managers.getDefault();
        assertEquals(0, tm.getHistory().size());
    }

    @Test
    public void shouldNotBeDuplicateTaskInHistory() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        tm.createTask(task1);
        tm.getTaskToId(1);
        tm.getTaskToId(1);

        assertEquals(1, tm.getHistory().size());
    }

    @Test
    public void shouldBeDeleteTaskFromStartHistory() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task2", "task2", 2, Status.IN_PROGRESS);
        Task task3 = new Task("task3", "task3", 3, Status.IN_PROGRESS);
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);

        tm.getTaskToId(1);
        tm.getTaskToId(2);
        tm.getTaskToId(3);

        tm.removeTaskById(1);

        assertEquals(2, tm.getHistory().size());
        assertEquals(2, tm.getHistory().get(0).getId());
        assertEquals(3, tm.getHistory().get(1).getId());
    }

    @Test
    public void shouldBeDeleteTaskFromEndHistory() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task2", "task2", 2, Status.IN_PROGRESS);
        Task task3 = new Task("task3", "task3", 3, Status.IN_PROGRESS);
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);

        tm.getTaskToId(1);
        tm.getTaskToId(2);
        tm.getTaskToId(3);

        tm.removeTaskById(3);

        assertEquals(2, tm.getHistory().size());
        assertEquals(1, tm.getHistory().get(0).getId());
        assertEquals(2, tm.getHistory().get(1).getId());
    }

    @Test
    public void shouldBeDeleteTaskFromMiddleHistory() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task2", "task2", 2, Status.IN_PROGRESS);
        Task task3 = new Task("task3", "task3", 3, Status.IN_PROGRESS);
        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);

        tm.getTaskToId(1);
        tm.getTaskToId(2);
        tm.getTaskToId(3);

        tm.removeTaskById(2);

        assertEquals(2, tm.getHistory().size());
        assertEquals(1, tm.getHistory().get(0).getId());
        assertEquals(3, tm.getHistory().get(1).getId());
    }

}