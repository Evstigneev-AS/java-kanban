package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final MyLinkedList history = new MyLinkedList();
    private final Map<Long, Node> historyMap = new HashMap<>();


    @Override
    public void add(Task task) {
        this.history.linkLast(task);
    }

    @Override
    public void remove(Long id) {
        history.removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    public class MyLinkedList {

        public Node<Task> head = null;//голова
        public Node<Task> tail = null;//хвост
        private int size = 0;

        public void linkLast(Task task) {
            Node nodeFromHistoryMap = historyMap.get(task.getId());
            if (nodeFromHistoryMap != null) {
                removeNode(nodeFromHistoryMap);
            }
            Node<Task> node = new Node<>(tail, task, null);
            if (tail == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
            size = size + 1;
            historyMap.put(task.getId(), node);
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> historyList = new ArrayList<>();
            Node<Task> node = tail;
            while (node != null) {
                historyList.add(node.data);
                node = node.prev;
            }
            return historyList;
        }

        public void removeNode(Node removeNode) {
            if (removeNode == null) return;
            if (removeNode.next != null) {
                removeNode.next.prev = removeNode.prev;
            }
            if (removeNode.prev != null) {
                removeNode.prev.next = removeNode.next;
            }
            if (tail.equals(removeNode)) {
                tail = removeNode.prev;
            }
            if (head.equals(removeNode)) {
                head = removeNode.next;
            }
            size = size - 1;
        }
    }
}
