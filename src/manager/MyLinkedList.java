package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLinkedList {

    transient Node<Task> first;
    transient Node<Task> last;

    private Map<Integer, Node<Task>> map = new HashMap<>();

    public void linkLast(Task element) {
        Node<Task> newNode = new Node<>(last, element, null);
        map.put(element.getId(), newNode);
        if (first == null) {
            first = newNode;
            last = newNode;
            return;
        }
        last.next = newNode;
        last = newNode;
    }

    public void removes(Task data) {
        Node<Task> taskNode = map.get(data.getId());
        if (taskNode == null) {
            return;
        }
        if (first.data.getId() == data.getId()) {
            first = first.next;
            map.remove(data.getId());
        } else if (last.data.getId() == data.getId()) {
            last.prev.next = null;
            last = last.prev;
            map.remove(data.getId());
        } else {
            taskNode.prev.next = taskNode.next;
            taskNode.next.prev = taskNode.prev;
            map.remove(data.getId());
        }
    }

    public List<Task> getAll() {
        Node<Task> node = first;
        List<Task> list = new ArrayList<>();
        while (node != null) {
            list.add(node.data);
            node = node.next;
        }
        return list;
    }

    static class Node<R> {
        public R data;
        public Node<R> next;
        public Node<R> prev;

        public Node(Node<R> prev, R data, Node<R> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}

