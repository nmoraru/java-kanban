package taskmanager.manager;

import org.junit.jupiter.api.Test;
import taskmanager.data.Status;
import taskmanager.data.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    public void shouldBeSaveOldVersionTaskInHistory() {
        TaskManager tm = Managers.getDefault();
        List<Task> listTask = new ArrayList<>();
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task1_update", "task1_update", 1, Status.DONE);

        listTask.add(task1);
        listTask.add(task2);
        tm.createTask(task1);
        tm.getTaskToId(1);
        tm.updateTask(task2);
        tm.getTaskToId(1);

        assertEquals(listTask, tm.getHistory());
    }

}