package taskmanager.data;

import org.junit.jupiter.api.Test;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubtaskTest {

    @Test
    public void mustBeEqualTwoSubtasksIfIDsEqual() {
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 8, Status.NEW, 4);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 8, Status.IN_PROGRESS, 4);

        assertEquals(subtask1, subtask2);
    }

    @Test
    public void mustNotBeEqualTwoSubtasksIfIDsNoEqual() {
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 8, Status.NEW, 4);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 9, Status.IN_PROGRESS, 4);

        assertNotEquals(subtask1, subtask2);
    }

    @Test
    public void shouldNotCreatedSubtaskIfNoExistEpicToParamEpicId() {
        TaskManager tm = Managers.getDefault();
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 1, Status.NEW, 1);

        tm.createSubtask(subtask1);
        Collection<Subtask> actualListSubtasks = tm.getAllSubtasks();

        assertEquals(0, actualListSubtasks.size());
    }
}