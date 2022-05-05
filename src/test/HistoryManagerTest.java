package test;

import manager.InMemoryTaskManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {      /* Тестируем методы интерфейса HistoryManager
                                   List<Task> getHistory(); void add(Task task); void remove(Task task) и проверяем на дублирование задачи*/


    InMemoryTaskManager inMemoryTaskManager;
    Task task;
    String startTime;
    int duration;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task();
        startTime = "2002-01-01T01:00:00.000000000";
        duration = 3;
        inMemoryTaskManager.createTask(task, startTime, duration);
        inMemoryTaskManager.getAnyTaskById(task.getId());
    }

    @Test
    void getHistory() {
        assertFalse(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().isEmpty(), "История пустая");
        inMemoryTaskManager.clearTaskById(task.getId());
        assertTrue(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().isEmpty(), "История не пустая");
    }

    @Test
    void addTask() {
        assertFalse(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().isEmpty(), "История пустая");
        Task task1 = new Task();
        startTime = "2006-01-01T01:00:00.000000000";
        duration = 3;
        inMemoryTaskManager.createTask(task1, startTime, duration);
        inMemoryTaskManager.getInMemoryHistoryManager().add(task1);
        assertEquals(task1, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().get(1), "В истории нет добавленной задачи");
    }

    @Test
    void removeTask() {
        assertFalse(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().isEmpty(), "История пустая");
        inMemoryTaskManager.getInMemoryHistoryManager().remove(task);
        assertTrue(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().isEmpty(), "История не пустая");
    }

    @Test
    void checkHistoryOnDubbleTask() {
        assertEquals(1, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().size(), "В истории нет добавленной задачи");
        inMemoryTaskManager.getAnyTaskById(task.getId());
        inMemoryTaskManager.getAnyTaskById(task.getId());
        assertEquals(1, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().size(), "Появилось дублирование задачи задачи");
    }
}