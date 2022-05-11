package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager, Serializable {
    private final MyLinkedList linkedHistory = new MyLinkedList();

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getAll();
    }

    @Override
    public void add(Task task) {
        linkedHistory.removes(task);
        linkedHistory.linkLast(task);
    }

    @Override
    public void remove(Task task) {
        linkedHistory.removes(task);
        if (task instanceof Epic) {
            HashMap<Integer, Subtask> subTaskMap = ((Epic) task).getSubTasksList();
            for (Subtask subtask : subTaskMap.values()) {
                linkedHistory.removes(subtask);
            }
        }
    }
}
