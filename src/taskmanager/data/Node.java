package taskmanager.data;

<<<<<<< HEAD
public class Node<T> {
=======
public class Node <T> {
>>>>>>> origin/sprint_6-solution

    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
