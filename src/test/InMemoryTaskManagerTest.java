package test;

import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest{           //здесь тестируются методы класса InMemoryTaskManager
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void changeEpicTime() {
        Epic epic = new Epic();
        startTime = "2020-01-01T01:00:00.000000000";
        duration = 3;
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        Subtask subtask = new Subtask();
        startTime1 = "2020-02-01T02:00:00.000000000";
        duration = 3;
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration);
        Subtask subtask1 = new Subtask();
        startTime2 = "2020-03-01T03:00:00.000000000";
        duration = 3;
        inMemoryTaskManager.createSubTask(epic, subtask1, startTime2, duration);
        assertEquals(epic.getStartTime(), subtask.getStartTime(), "Время начала эпика и 1й сабтаски не совпадает");
        assertEquals(epic.getEndTime(), subtask1.getEndTime(), "Время конца эпика и последней сабтаски не совпадает");
    }
}