import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;
    private int maxSize = 10;

    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null){
            history.add(task);

            if(history.size() > 10){
                history.remove(0);
            }
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private void linkLast(Node node){
        if (tail == null){
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null){
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task){
            this.task = task;
        }
    }
}
