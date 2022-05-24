package test;

import manager.HTTPTaskManager;
import manager.KVServer;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest {   //здесь тестируются методы класса HTTPTaskManager,

    @BeforeAll
    static void beforeAll() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
    }
    HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");

    @Test
    public void saveAndLoadTask() {
        Task task = new Task();
        String startTime = "2000-01-01T01:00:00.000000000";
        int duration = 3;
        String key = "taskList";
        httpTaskManager.save(key, httpTaskManager.createTask(task, startTime, duration));
        assertEquals(httpTaskManager.getTasksList().toString(), httpTaskManager.loadFromServer(key), "Сохранения не произошло");

    }

    @Test
    public void saveAndLoadWithEpicOutSubtasks() {
        Epic epic = new Epic();
        String startTime = "2000-02-01T01:00:00.000000000";
        int duration = 3;
        String key = "epicList";
        httpTaskManager.save(key, httpTaskManager.createEpic(epic, startTime, duration));
        assertEquals(httpTaskManager.getEpicsList().toString(), httpTaskManager.loadFromServer(key), "Сохранения не произошло");
    }

    @Test
    public void saveAndLoadHistoryList() {
        Task task = new Task();
        String startTime = "2000-03-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createTask(task, startTime, duration);
        httpTaskManager.getAnyTaskById(task.getId());
        String key = "history";
        httpTaskManager.save(key, httpTaskManager.getInMemoryHistoryManager().getHistory());
        assertEquals(httpTaskManager.getInMemoryHistoryManager().getHistory().toString(), httpTaskManager.loadFromServer(key), "Сохранения не произошло");
    }

    @Test
    public void saveAndLoadPrioritizedTasksList() {
        Task task = new Task();
        String startTime = "2000-03-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createTask(task, startTime, duration);
        String key = "prioritizedTasksList";
        httpTaskManager.save(key, httpTaskManager.getPrioritizedTasksList());
        assertEquals(httpTaskManager.getPrioritizedTasksList().toString(), httpTaskManager.loadFromServer(key), "Сохранения не произошло");
    }
}
