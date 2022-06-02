package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager, Serializable {
    private KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient(url);
    }

    public void save(){
        Gson gson = new Gson();
        kvTaskClient.put("taskList", gson.toJson(this.getTasksList()));
        kvTaskClient.put("epicList", gson.toJson(this.getEpicsList()));
        kvTaskClient.put("history", gson.toJson(this.getInMemoryHistoryManager().getHistory()));
        kvTaskClient.put("prioritizedTasksList", gson.toJson(this.getPrioritizedTasksList()));
    }

    public HashMap load(String key) {
        Gson gson = new Gson();
        HashMap object = null;
        if (key == "taskList"){
            HashMap<Integer, Task> fromGsonTaskList = gson.fromJson(kvTaskClient.load(key), new TypeToken<HashMap<Integer, Task>>(){}.getType());
            object = fromGsonTaskList;
        } else if (key == "epicList"){
            HashMap<Integer, Epic> fromGsonEpicList = gson.fromJson(kvTaskClient.load(key), new TypeToken<HashMap<Integer, Epic>>(){}.getType());
            object = fromGsonEpicList;
        } else if (key == "history"){
            List<Task> fromGsonHistory = gson.fromJson(kvTaskClient.load(key), new TypeToken<List<Task>>(){}.getType());
            object = new HashMap<>();
            String task = "history";
            object.put(task, fromGsonHistory.toString());
        } else if (key == "prioritizedTasksList"){
            ArrayList<Task> fromGsonPrioritizedTasksList = gson.fromJson(kvTaskClient.load(key), new TypeToken<ArrayList<Task>>(){}.getType());
            object = new HashMap<>();
            String task = "prioritizedTasksList";
            object.put(task, fromGsonPrioritizedTasksList.toString());
        }
        return object;
    }

}
