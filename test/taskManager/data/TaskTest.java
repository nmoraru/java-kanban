package taskManager.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void mustBeEqualTwoTasksIfIDsEqual() {
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task2", "task2", 1, Status.DONE);

        assertEquals(task1, task2);
    }

    @Test
    public void mustNotBeEqualTwoTasksIfIDsNoEqual() {
        Task task1 = new Task("task1", "task1", 1, Status.IN_PROGRESS);
        Task task2 = new Task("task2", "task2", 2, Status.DONE);

        assertNotEquals(task1, task2);
    }
}