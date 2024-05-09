package taskmanager.manager;

import org.junit.jupiter.api.Test;
import taskmanager.data.Status;
import taskmanager.data.Task;

import java.util.ArrayList;
import java.util.List;

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

}