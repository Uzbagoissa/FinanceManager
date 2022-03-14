package manager;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {//ggg

    ArrayList<Task> getHistory();

    void add(Task task);
}
