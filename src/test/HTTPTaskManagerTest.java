package test;

import manager.HTTPTaskManager;
import manager.KVServer;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest {   //здесь тестируются методы класса HTTPTaskManager

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
        httpTaskManager.createTask(task, startTime, duration);
        String key = "taskList";
        httpTaskManager.save();
        assertEquals(task.toString(), httpTaskManager.load(key).get(1).toString(), "Сохранения не произошло");

    }

    @Test
    public void saveAndLoadWithEpicNoSubtasks() {
        Epic epic = new Epic();
        String startTime = "2000-02-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createEpic(epic, startTime, duration);
        String key = "epicList";
        httpTaskManager.save();
        assertEquals(epic.toString(), httpTaskManager.load(key).get(1).toString(), "Сохранения не произошло");
    }

    @Test
    public void saveAndLoadWithEpicWithSubtasks() {
        Epic epic = new Epic();
        String startTime = "2000-02-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createEpic(epic, startTime, duration);
        Subtask subtask = new Subtask();
        String startTime1 = "2000-03-01T01:00:00.000000000";
        int duration1 = 3;
        httpTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        String key = "epicList";
        httpTaskManager.save();
        Epic epic4 = (Epic) httpTaskManager.load(key).get(1);
        assertEquals(subtask.toString(), epic4.getSubTasksList().get(2).toString(), "Сохранения не произошло");
    }

    @Test
    public void saveAndLoadHistoryList() {
        Task task = new Task();
        String startTime = "2000-03-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createTask(task, startTime, duration);
        httpTaskManager.getAnyTaskById(task.getId());
        String key = "history";
        httpTaskManager.save();
        assertEquals("{history=" + httpTaskManager.getInMemoryHistoryManager().getHistory().toString() + "}", httpTaskManager.load(key).toString(), "Сохранения не произошло");
    }

    @Test
    public void saveAndLoadPrioritizedTasksList() {
        Task task = new Task();
        String startTime = "2000-03-01T01:00:00.000000000";
        int duration = 3;
        httpTaskManager.createTask(task, startTime, duration);
        String key = "prioritizedTasksList";
        httpTaskManager.save();
        assertEquals("{prioritizedTasksList=" + httpTaskManager.getPrioritizedTasksList().toString() + "}", httpTaskManager.load(key).toString(), "Сохранения не произошло");
    }
}
