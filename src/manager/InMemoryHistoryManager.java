package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    MyLinkedList linkedHystory = new MyLinkedList();

    @Override
    public List<Task> getHistory() {
        return linkedHystory.getAll();
    }

    @Override
    public void add(Task task){
        linkedHystory.removes(task);
        linkedHystory.linkLast(task);
    }

    @Override
    public void remove(Task task) {
        linkedHystory.removes(task);
        if (task instanceof Epic){
            HashMap<Integer, Subtask> dfxfg = ((Epic) task).getSubTasksList();
            for (Subtask subtask : dfxfg.values()) {
                linkedHystory.removes(subtask);
            }
        }
    }
}
