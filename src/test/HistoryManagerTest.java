package test;

import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {      /* Тестируем методы интерфейса HistoryManager
                           List<Task> getHistory(); void add(Task task); void remove(Task task)*/

    @Test
    void getHistoryAddTaskRemoveTask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = new Task();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.getInMemoryHistoryManager().add(task);
        assertEquals(task, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().get(0), "История пустая");
        inMemoryTaskManager.getInMemoryHistoryManager().add(task);
        assertEquals(1, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().size(), "Есть дублирование Task задачи");
        inMemoryTaskManager.getInMemoryHistoryManager().remove(task);
        assertEquals(0, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().size(), "Задача Task осталась в истории");
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask =new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.getInMemoryHistoryManager().add(epic);
        assertEquals(epic, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().get(0), "История пустая");
        inMemoryTaskManager.getInMemoryHistoryManager().remove(epic);
        assertEquals(0, inMemoryTaskManager.getInMemoryHistoryManager().getHistory().size(), "Задача Subtask осталась в истории");
    }

}