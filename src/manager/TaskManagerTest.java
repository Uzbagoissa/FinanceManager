package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {     //тестируем методы интерфейса TaskManager
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void createTask() {
        Task task = new Task();
        assertFalse(inMemoryTaskManager.getTasksList().containsValue(task), "Список не пуст. В нем Task задача!");
        inMemoryTaskManager.createTask(task);
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(task), "Список пуст!");
    }

    @Test
    public void createEpic() {
        Epic epic = new Epic();
        assertFalse(inMemoryTaskManager.getEpicsList().containsValue(epic), "Список не пуст. В нем Epic задача!");
        inMemoryTaskManager.createEpic(epic);
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(epic), "Список пуст!");
    }

    @Test
    public void createSubTask() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        assertFalse(epic.getSubTasksList().containsValue(subtask), "Список не пуст. В нем Subtask задача!");
        inMemoryTaskManager.createSubTask(epic, subtask);
        assertTrue(epic.getSubTasksList().containsValue(subtask), "Список пуст!");
    }

    @Test
    public void clearAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        assertFalse(inMemoryTaskManager.getTasksList().isEmpty(), "Список пуст!");
        inMemoryTaskManager.clearAllTasks();
        assertTrue(inMemoryTaskManager.getTasksList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllEpic() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createEpic(epic3);
        assertFalse(inMemoryTaskManager.getEpicsList().isEmpty(), "Список пуст!");
        inMemoryTaskManager.clearAllEpic();
        assertTrue(inMemoryTaskManager.getEpicsList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllSubTasks() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        inMemoryTaskManager.createSubTask(epic, subtask3);
        assertFalse(epic.getSubTasksList().isEmpty(), "Список пуст!");
        inMemoryTaskManager.clearAllSubTasks(epic.getId());
        assertTrue(epic.getSubTasksList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void getAnyTaskById() {
        Task task = new Task();
        Epic epic = new Epic();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        assertEquals(task, inMemoryTaskManager.getAnyTaskById(1), "Метод не работает");
        assertEquals(epic, inMemoryTaskManager.getAnyTaskById(2), "Метод не работает");
        assertEquals(null, inMemoryTaskManager.getAnyTaskById(3), "Метод странно работает");
    }

    @Test
    public void getSubTaskById() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        assertEquals(subtask, inMemoryTaskManager.getSubTaskById(1, 2), "Метод не работает");
        assertEquals(null, inMemoryTaskManager.getSubTaskById(1, 3), "Метод странно работает");
    }

    @Test
    public void renewTaskById() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(task2), "В списке нет task2");
        Task newTask = new Task();
        inMemoryTaskManager.renewTaskById(newTask, 2);
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(newTask), "Новой newTask задачи нет в списке");
        assertFalse(inMemoryTaskManager.getTasksList().containsValue(task2), "Старая задача task2 все еще в списке!");
    }

    @Test
    public void renewEpicById() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createEpic(epic3);
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(epic2), "В списке нет epic2");
        Epic newEpic = new Epic();
        inMemoryTaskManager.renewEpicById(newEpic, 2);
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(newEpic), "Новой newEpic задачи нет в списке");
        assertFalse(inMemoryTaskManager.getEpicsList().containsValue(epic2), "Старая задача epic2 все еще в списке!");
    }

    @Test
    public void renewSubtaskById() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        inMemoryTaskManager.createSubTask(epic, subtask3);
        assertTrue(epic.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        Subtask newSubtask = new Subtask();
        inMemoryTaskManager.renewSubTaskById(epic, newSubtask, 3);
        assertTrue(epic.getSubTasksList().containsValue(newSubtask), "Новой newSubtask задачи нет в списке");
        assertFalse(epic.getSubTasksList().containsValue(subtask2), "Старая задача subtask2 все еще в списке!");
    }

    @Test
    public void clearTaskById() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(task2), "В списке нет task2");
        inMemoryTaskManager.clearTaskById(2);
        assertFalse(inMemoryTaskManager.getTasksList().containsValue(task2), "В списке есть task2");
    }

    @Test
    public void clearEpicById() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createEpic(epic3);
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(epic2), "В списке нет epic2");
        inMemoryTaskManager.clearEpicById(2);
        assertFalse(inMemoryTaskManager.getEpicsList().containsValue(epic2), "В списке есть epic2");
    }

    @Test
    public void clearSubTaskById() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        inMemoryTaskManager.createSubTask(epic, subtask3);
        assertTrue(epic.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        inMemoryTaskManager.clearSubTaskById(epic, 3);
        assertFalse(epic.getSubTasksList().containsValue(subtask2), "В списке есть subtask2");
    }

    @Test
    public void getTaskStatusById() {
        Task task = new Task();
        inMemoryTaskManager.createTask(task);
        assertEquals(Status.NEW.toString(), inMemoryTaskManager.getTaskStatusById(1), "Метод не работает");
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), inMemoryTaskManager.getTaskStatusById(1), "Метод не работает");
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), inMemoryTaskManager.getTaskStatusById(1), "Метод не работает");
    }

    @Test
    public void getEpicStatusById() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        assertEquals(Status.NEW.toString(), inMemoryTaskManager.getEpicStatusById(1), "Метод не работает");
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), inMemoryTaskManager.getEpicStatusById(1), "Метод не работает");
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), inMemoryTaskManager.getEpicStatusById(1), "Метод не работает");
    }

}