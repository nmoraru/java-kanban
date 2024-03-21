package taskManager.manager;

import taskManager.data.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_MAX_SIZE = 10;

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
            if (history.size() > HISTORY_MAX_SIZE) {
                history.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList(history);
    }
}
