package taskmanager.data;

import org.junit.jupiter.api.Test;
import taskmanager.manager.Managers;
import taskmanager.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EpicTest {

    @Test
    public void mustBeEqualTwoEpicsIfIDsEqual() {
        Epic epic1 = new Epic("epic1", "epic1", 4);
        Epic epic2 = new Epic("epic2", "epic2", 4);

        assertEquals(epic1, epic2);
    }

    @Test
    public void mustNotBeEqualTwoEpicsIfIDsNoEqual() {
        Epic epic1 = new Epic("epic1", "epic1", 4);
        Epic epic2 = new Epic("epic2", "epic2", 5);

        assertNotEquals(epic1, epic2);
    }

    @Test
    public void shouldBeEpicStatusNewIfAllEpicSubtasksInStatusNew() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.NEW, 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, Status.NEW, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);

        assertEquals(Status.NEW, tm.getEpicToId(1).getStatus());
    }

    @Test
    public void shouldBeEpicStatusDoneIfAllEpicSubtasksInStatusDone() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.DONE, 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, Status.DONE, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);

        assertEquals(Status.DONE, tm.getEpicToId(1).getStatus());
    }

    @Test
    public void shouldBeEpicStatusInProgressIfAllEpicSubtasksInStatusInProgress() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, Status.IN_PROGRESS, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, tm.getEpicToId(1).getStatus());
    }

    @Test
    public void shouldBeEpicStatusInProgressIfEpicSubtasksInStatusNewAndDone() {
        TaskManager tm = Managers.getDefault();
        Epic epic1 = new Epic("epic1", "epic1", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1", 2, Status.NEW, 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, Status.DONE, 1);

        tm.createEpic(epic1);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, tm.getEpicToId(1).getStatus());
    }

}