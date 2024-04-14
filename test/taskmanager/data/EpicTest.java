package taskmanager.data;

import org.junit.jupiter.api.Test;

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

    // В моей реализации метод может принять только сабтаску addSubtaskToEpic(Subtask subtask).
    // Эпик и таску не передать - ошибка компиляции.
//    @Test
//    public void Test() {
//        Epic epic1 = new Epic("epic1", "epic1", 1);
//        epic1.addSubtaskToEpic(epic1);
//    }

}