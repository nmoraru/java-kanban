package taskmanager.manager;

import taskmanager.data.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList(history);
    }
}
