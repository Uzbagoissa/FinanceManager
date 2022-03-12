package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        while (true){
            if (history.size() > 10){
                history.remove(0);
            } else if (history.size() <= 10){
                break;
            }
        }
        return history;
    }

    @Override
    public void add(Task task){
        history.add(task);
    }
}
