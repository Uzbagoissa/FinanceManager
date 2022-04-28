package manager;

import java.io.Serializable;

public class Managers implements Serializable {
    private Managers(){};

    public TaskManager getDefault(){
        return new InMemoryTaskManager ();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}