package manager;

public class Managers {
    private Managers(){};

    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
