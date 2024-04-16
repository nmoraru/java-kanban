package taskmanager.manager;

import taskmanager.data.Node;
import taskmanager.data.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> mapHistory = new LinkedHashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (mapHistory.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
        }
    }

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);
        if (mapHistory.isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            Node<Task> oldHead = head;
            oldHead.next = newNode;
            newNode.prev = oldHead;
            head = newNode;
        }
        mapHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        Collection<Node<Task>> listNodes = mapHistory.values();
        ArrayList<Task> listResult = new ArrayList<>();
        for (Node<Task> node : listNodes) {
            listResult.add(node.data);
        }
        return listResult;
    }

    @Override
    public void remove(int id) {
        if (mapHistory.containsKey(id)) {
            Node<Task> node = mapHistory.get(id);
            removeNode(node);
            mapHistory.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> prevNode = node.prev;
            Node<Task> nextNode = node.next;

            if (mapHistory.size() == 1) {
                head = null;
                tail = null;
            } else if (nextNode == null) {
                prevNode.next = null;
                head = prevNode;
                node.prev = null;
                node.next = null;
            } else if (prevNode == null) {
                nextNode.prev = null;
                tail = nextNode;
                node.prev = null;
                node.next = null;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                node.prev = null;
                node.next = null;
            }
        }
    }
}
