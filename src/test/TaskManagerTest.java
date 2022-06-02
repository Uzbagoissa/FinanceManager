package test;

import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    String startTime;
    String startTime1;
    String startTime2;
    String startTime3;
    int duration;
    Task task1;
    Task task2;
    Task task3;
    Epic epic1;
    Epic epic2;
    Epic epic3;

    @Test
    public void allTest(){}

    @BeforeEach
    public void beforeEach() {
        startTime = "2000-01-01T01:00:00.000000000";
        startTime1 = "2000-02-01T01:00:00.000000000";
        startTime2 = "2001-01-01T01:00:00.000000000";
        startTime3 = "2002-01-01T01:00:00.000000000";
        duration = 3;
        task1 = new Task();
        task2 = new Task();
        task3 = new Task();
        epic1 = new Epic();
        epic2 = new Epic();
        epic3 = new Epic();
    }

    @Test
    public void createTask() {
        taskManager.createTask(task1, startTime1, duration);
        assertEquals(task1, taskManager.getAnyTaskById(1), "Задача не создалась");
    }

    @Test
    public void createEpic() {
        taskManager.createEpic(epic1, startTime1, duration);
        assertEquals(epic1, taskManager.getAnyTaskById(1), "Задача не создалась");
    }

    @Test
    public void createSubTask() {
        taskManager.createEpic(epic1, startTime1, duration);
        Subtask subtask = new Subtask();
        taskManager.createSubTask(epic1, subtask, startTime1, duration);
        assertEquals(epic1, taskManager.getAnyTaskById(1), "Задача не создалась");
    }

    @Test
    public void clearAllTasks() {
        taskManager.createTask(task1, startTime1, duration);
        taskManager.createTask(task2, startTime2, duration);
        taskManager.createTask(task3, startTime3, duration);
        assertFalse(taskManager.getTasksList().isEmpty(), "Список пуст!");
        taskManager.clearAllTasks();
        assertTrue(taskManager.getTasksList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllEpic() {
        taskManager.createEpic(epic1, startTime1, duration);
        taskManager.createEpic(epic2, startTime2, duration);
        taskManager.createEpic(epic3, startTime3, duration);
        assertFalse(taskManager.getEpicsList().isEmpty(), "Список пуст!");
        taskManager.clearAllEpic();
        assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllSubTasks() {
        taskManager.createEpic(epic1, startTime1, duration);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        taskManager.createSubTask(epic1, subtask1, startTime1, duration);
        taskManager.createSubTask(epic1, subtask2, startTime2, duration);
        taskManager.createSubTask(epic1, subtask3, startTime3, duration);
        assertFalse(epic1.getSubTasksList().isEmpty(), "Список пуст!");
        taskManager.clearAllSubTasks(epic1.getId());
        assertTrue(epic1.getSubTasksList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void getAnyTaskById() {
        taskManager.createTask(task1, startTime1, duration);
        taskManager.createEpic(epic1, startTime2, duration);
        assertEquals(task1, taskManager.getAnyTaskById(1), "Метод не работает");
        assertEquals(epic1, taskManager.getAnyTaskById(2), "Метод не работает");
        assertNull(taskManager.getAnyTaskById(3), "Метод странно работает");
    }

    @Test
    public void getSubTaskById() {
        taskManager.createEpic(epic1, startTime, duration);
        Subtask subtask = new Subtask();
        taskManager.createSubTask(epic1, subtask, startTime1, duration);
        assertEquals(subtask, taskManager.getSubTaskById(1, 2), "Метод не работает");
        assertNull(taskManager.getSubTaskById(1, 3), "Метод странно работает");
    }

    @Test
    public void renewTaskById() {
        taskManager.createTask(task1, startTime1, duration);
        taskManager.createTask(task2, startTime2, duration);
        taskManager.createTask(task3, startTime3, duration);
        assertTrue(taskManager.getTasksList().containsValue(task2), "В списке нет task2");
        Task newTask = new Task();
        taskManager.renewTaskById(newTask, 2);
        assertTrue(taskManager.getTasksList().containsValue(newTask), "Новой newTask задачи нет в списке");
        assertFalse(taskManager.getTasksList().containsValue(task2), "Старая задача task2 все еще в списке!");
    }

    @Test
    public void renewEpicById() {
        taskManager.createEpic(epic1, startTime1, duration);
        taskManager.createEpic(epic2, startTime2, duration);
        taskManager.createEpic(epic3, startTime3, duration);
        assertTrue(taskManager.getEpicsList().containsValue(epic2), "В списке нет epic2");
        Epic newEpic = new Epic();
        taskManager.renewEpicById(newEpic, 2);
        assertTrue(taskManager.getEpicsList().containsValue(newEpic), "Новой newEpic задачи нет в списке");
        assertFalse(taskManager.getEpicsList().containsValue(epic2), "Старая задача epic2 все еще в списке!");
    }

    @Test
    public void renewSubtaskById() {
        taskManager.createEpic(epic1, startTime, duration);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        taskManager.createSubTask(epic1, subtask1, startTime1, duration);
        taskManager.createSubTask(epic1, subtask2, startTime2, duration);
        taskManager.createSubTask(epic1, subtask3, startTime3, duration);
        assertTrue(epic1.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        Subtask newSubtask = new Subtask();
        taskManager.renewSubTaskById(epic1, newSubtask, 3);
        assertTrue(epic1.getSubTasksList().containsValue(newSubtask), "Новой newSubtask задачи нет в списке");
        assertFalse(epic1.getSubTasksList().containsValue(subtask2), "Старая задача subtask2 все еще в списке!");
    }

    @Test
    public void clearTaskById() {
        taskManager.createTask(task1, startTime1, duration);
        taskManager.createTask(task2, startTime2, duration);
        taskManager.createTask(task3, startTime3, duration);
        assertTrue(taskManager.getTasksList().containsValue(task2), "В списке нет task2");
        taskManager.clearTaskById(2);
        assertFalse(taskManager.getTasksList().containsValue(task2), "В списке есть task2");
    }

    @Test
    public void clearEpicById() {
        taskManager.createEpic(epic1, startTime1, duration);
        taskManager.createEpic(epic2, startTime2, duration);
        taskManager.createEpic(epic3, startTime3, duration);
        assertTrue(taskManager.getEpicsList().containsValue(epic2), "В списке нет epic2");
        taskManager.clearEpicById(2);
        assertFalse(taskManager.getEpicsList().containsValue(epic2), "В списке есть epic2");
    }

    @Test
    public void clearSubTaskById() {
        taskManager.createEpic(epic1, startTime, duration);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        taskManager.createSubTask(epic1, subtask1, startTime1, duration);
        taskManager.createSubTask(epic1, subtask2, startTime2, duration);
        taskManager.createSubTask(epic1, subtask3, startTime3, duration);
        assertTrue(epic1.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        taskManager.clearSubTaskById(epic1, 3);
        assertFalse(epic1.getSubTasksList().containsValue(subtask2), "В списке есть subtask2");
    }

    @Test
    public void getTaskStatusById() {
        taskManager.createTask(task1, startTime1, duration);
        assertEquals(Status.NEW.toString(), taskManager.getTaskStatusById(1), "Метод не работает");
        task1.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), taskManager.getTaskStatusById(1), "Метод не работает");
        task1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), taskManager.getTaskStatusById(1), "Метод не работает");
    }

    @Test
    public void getEpicStatusById() {
        taskManager.createEpic(epic1, startTime1, duration);
        assertEquals(Status.NEW.toString(), taskManager.getEpicStatusById(1), "Метод не работает");
        epic1.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), taskManager.getEpicStatusById(1), "Метод не работает");
        epic1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), taskManager.getEpicStatusById(1), "Метод не работает");
    }

}
