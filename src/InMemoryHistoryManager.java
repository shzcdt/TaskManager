import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;
    private int maxSize = 10;


    @Override
    public void add(Task task) {
        int id = task.getId();

        if (nodeMap.containsKey(id)) {
            Node oldNode = nodeMap.get(id);
            removeNode(oldNode);
        }

        Node newNode = new Node(task);
        linkLast(newNode);
        nodeMap.put(id, newNode);

        if (nodeMap.size() > maxSize) {
            if (head != null) {
                nodeMap.remove(head.task.getId());
                removeNode(head);
            }
        }
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.get(id);
            removeNode(node);

            nodeMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);

            current = current.next;
        }
        return history;
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
