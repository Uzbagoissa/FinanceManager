package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    final static ArrayList<Task> SAVESHOWOBJECTS = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        while (true){
            if (SAVESHOWOBJECTS.size() > 10){
                SAVESHOWOBJECTS.remove(0);
            } else if (SAVESHOWOBJECTS.size() <= 10){
                break;
            }
        }
        return SAVESHOWOBJECTS;
    }

    @Override
    public void add(Task task){
        SAVESHOWOBJECTS.add(task);
    }
}
