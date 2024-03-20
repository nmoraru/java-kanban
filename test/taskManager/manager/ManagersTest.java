package taskManager.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    public void shouldBeCreateDefaultManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    public void shouldBeCreateDefaultHistoryManager() {
        HistoryManager manager = Managers.getDefaultHistory();
        assertNotNull(manager);
    }

}