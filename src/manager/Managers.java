package manager;

import java.io.Serializable;

public class Managers implements Serializable {
    private Managers(){};
    static String url = "http://localhost:8078/";

    public static HTTPTaskManager getDefault(){
        return new HTTPTaskManager (url);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
