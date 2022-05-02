package test;

import manager.FileBackedTasksManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {
    String dir = System.getProperty("user.dir");
    File file = new File("backend.txt");
    FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void beforeEach() {          //тестируем методы FileBackedTasksManager
        fileBackedTasksManager = new FileBackedTasksManager(file, dir);
    }

    @Test
    public void startAndFinishTask() throws IOException, ClassNotFoundException {
        Task task = new Task();
        String startTime = "now";
        String finishTime = "now";
        fileBackedTasksManager.createTask(task, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.startTask(task.getId(), startTime, file, dir, fileBackedTasksManager);
        assertTrue(task.getStartTime() != null, "Выполнение задачи не началось");
        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Выполнение задачи не началось");
        fileBackedTasksManager.finishTask(task.getId(), finishTime, file, dir, fileBackedTasksManager);
        assertTrue(task.getEndTime() != null, "Задача все еще выполняется");
        assertEquals(Duration.between(task.getStartTime(), task.getEndTime()), task.getDuration(), "Задача все еще выполняется");
        assertEquals(Status.DONE, task.getStatus(), "Задача все еще выполняется");
    }

    @Test
    public void startAndFinishEpicAndChangeEpicTime() throws IOException, ClassNotFoundException {
        Epic epic = new Epic();
        String startTime = "now";
        String finishTime = "now";
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        Subtask subtask1 = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask1);
        fileBackedTasksManager.startEpic(epic.getId(), subtask1.getId(), startTime, file, dir, fileBackedTasksManager);
        assertTrue(epic.getStartTime() != null, "Выполнение задачи не началось");
        assertTrue(epic.getStartTime() == subtask1.getStartTime(), "Выполнение задачи не началось");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Выполнение задачи не началось");
        assertEquals(Status.IN_PROGRESS, subtask1.getStatus(), "Выполнение задачи не началось");
        try{
            sleep(1000);
        }catch(InterruptedException e) {
        }
        fileBackedTasksManager.finishEpic(epic.getId(), subtask1.getId(), finishTime, file, dir, fileBackedTasksManager);
        assertTrue(epic.getEndTime() != null, "Задача все еще выполняется");
        assertTrue(epic.getEndTime() == subtask1.getEndTime(), "Задача все еще выполняется");
        assertEquals(Duration.between(epic.getStartTime(), epic.getEndTime()), epic.getDuration(), "Задача все еще выполняется");
        assertEquals(Status.DONE, epic.getStatus(), "Задача все еще выполняется");
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask2);
        fileBackedTasksManager.createSubTask(epic, subtask3);
        try{
            sleep(1000);
        }catch(InterruptedException e) {
        }
        fileBackedTasksManager.startEpic(epic.getId(), subtask2.getId(), startTime, file, dir, fileBackedTasksManager);
        try{
            sleep(1000);
        }catch(InterruptedException e) {
        }
        fileBackedTasksManager.finishEpic(epic.getId(), subtask2.getId(), finishTime, file, dir, fileBackedTasksManager);
        try{
            sleep(1000);
        }catch(InterruptedException e) {
        }
        fileBackedTasksManager.startEpic(epic.getId(), subtask3.getId(), startTime, file, dir, fileBackedTasksManager);
        try{
            sleep(1000);
        }catch(InterruptedException e) {
        }
        fileBackedTasksManager.finishEpic(epic.getId(), subtask3.getId(), finishTime, file, dir, fileBackedTasksManager);
        assertTrue(epic.getStartTime() == subtask1.getStartTime(), "Начало выполнения эпика не соответствует началу выполнения его 1й подзадачи");
        assertTrue(epic.getEndTime() == subtask3.getEndTime(), "Конец выполнения эпика не соответствует концу выполнения его последней подзадачи");
    }

    @Test
    public void saveAndLoad() throws IOException, ClassNotFoundException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.save(file, dir, fileBackedTasksManager);
        fileBackedTasksManager.loadFromFile(file, dir);
        assertEquals(epic, fileBackedTasksManager.getAnyTaskById(1, file, dir, fileBackedTasksManager), "Сохранения состояния не произошло");
        fileBackedTasksManager.clearAllEpic(file, dir, fileBackedTasksManager);
        fileBackedTasksManager.save(file, dir, fileBackedTasksManager);
        fileBackedTasksManager.loadFromFile(file, dir);
        assertEquals(null, fileBackedTasksManager.getAnyTaskById(1, file, dir, fileBackedTasksManager), "Сохранения состояния не произошло");
    }

    @Test
    public void createTask() throws IOException {
        Task task = new Task();
        assertFalse(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task), "Список не пуст. В нем Task задача!");
        fileBackedTasksManager.createTask(task, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task), "Список пуст!");
    }

    @Test
    public void createEpic() throws IOException {
        Epic epic = new Epic();
        assertFalse(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic), "Список не пуст. В нем Epic задача!");
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic), "Список пуст!");
    }

    @Test
    public void createSubTask() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        Subtask subtask = new Subtask();
        assertFalse(epic.getSubTasksList().containsValue(subtask), "Список не пуст. В нем Subtask задача!");
        fileBackedTasksManager.createSubTask(epic, subtask, file, dir, fileBackedTasksManager);
        assertTrue(epic.getSubTasksList().containsValue(subtask), "Список пуст!");
    }

    @Test
    public void clearAllTasks() throws IOException {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        fileBackedTasksManager.createTask(task1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task3, file, dir, fileBackedTasksManager);
        assertFalse(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).isEmpty(), "Список пуст!");
        fileBackedTasksManager.clearAllTasks(file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllEpic() throws IOException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        fileBackedTasksManager.createEpic(epic1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic3, file, dir, fileBackedTasksManager);
        assertFalse(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).isEmpty(), "Список пуст!");
        fileBackedTasksManager.clearAllEpic(file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void clearAllSubTasks() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask3, file, dir, fileBackedTasksManager);
        assertFalse(epic.getSubTasksList().isEmpty(), "Список пуст!");
        fileBackedTasksManager.clearAllSubTasks(epic.getId(), file, dir, fileBackedTasksManager);
        assertTrue(epic.getSubTasksList().isEmpty(), "Список не пуст! В нем Task задачи!");
    }

    @Test
    public void getAnyTaskById() throws IOException {
        Task task = new Task();
        Epic epic = new Epic();
        fileBackedTasksManager.createTask(task, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        assertEquals(task, fileBackedTasksManager.getAnyTaskById(1, file, dir, fileBackedTasksManager), "Метод не работает");
        assertEquals(epic, fileBackedTasksManager.getAnyTaskById(2, file, dir, fileBackedTasksManager), "Метод не работает");
        assertEquals(null, fileBackedTasksManager.getAnyTaskById(3, file, dir, fileBackedTasksManager), "Метод странно работает");
    }

    @Test
    public void getSubTaskById() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        Subtask subtask = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask, file, dir, fileBackedTasksManager);
        assertEquals(subtask, fileBackedTasksManager.getSubTaskById(1, 2, file, dir, fileBackedTasksManager), "Метод не работает");
        assertEquals(null, fileBackedTasksManager.getSubTaskById(1, 3, file, dir, fileBackedTasksManager), "Метод странно работает");
    }

    @Test
    public void renewTaskById() throws IOException {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        fileBackedTasksManager.createTask(task1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task3, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task2), "В списке нет task2");
        Task newTask = new Task();
        fileBackedTasksManager.renewTaskById(newTask, 2, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(newTask), "Новой newTask задачи нет в списке");
        assertFalse(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task2), "Старая задача task2 все еще в списке!");
    }

    @Test
    public void renewEpicById() throws IOException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        fileBackedTasksManager.createEpic(epic1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic3, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic2), "В списке нет epic2");
        Epic newEpic = new Epic();
        fileBackedTasksManager.renewEpicById(newEpic, 2, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(newEpic), "Новой newEpic задачи нет в списке");
        assertFalse(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic2), "Старая задача epic2 все еще в списке!");
    }

    @Test
    public void renewSubtaskById() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask3, file, dir, fileBackedTasksManager);
        assertTrue(epic.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        Subtask newSubtask = new Subtask();
        fileBackedTasksManager.renewSubTaskById(epic, newSubtask, 3, file, dir, fileBackedTasksManager);
        assertTrue(epic.getSubTasksList().containsValue(newSubtask), "Новой newSubtask задачи нет в списке");
        assertFalse(epic.getSubTasksList().containsValue(subtask2), "Старая задача subtask2 все еще в списке!");
    }

    @Test
    public void clearTaskById() throws IOException {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        fileBackedTasksManager.createTask(task1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createTask(task3, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task2), "В списке нет task2");
        fileBackedTasksManager.clearTaskById(2, file, dir, fileBackedTasksManager);
        assertFalse(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager).containsValue(task2), "В списке есть task2");
    }

    @Test
    public void clearEpicById() throws IOException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        fileBackedTasksManager.createEpic(epic1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createEpic(epic3, file, dir, fileBackedTasksManager);
        assertTrue(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic2), "В списке нет epic2");
        fileBackedTasksManager.clearEpicById(2, file, dir, fileBackedTasksManager);
        assertFalse(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager).containsValue(epic2), "В списке есть epic2");
    }

    @Test
    public void clearSubTaskById() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        fileBackedTasksManager.createSubTask(epic, subtask1, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask2, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.createSubTask(epic, subtask3, file, dir, fileBackedTasksManager);
        assertTrue(epic.getSubTasksList().containsValue(subtask2), "В списке нет subtask2");
        fileBackedTasksManager.clearSubTaskById(epic, 2, file, dir, fileBackedTasksManager);
        assertFalse(epic.getSubTasksList().containsValue(subtask2), "В списке есть subtask2");
    }

    @Test
    public void getTaskStatusById() throws IOException {
        Task task = new Task();
        fileBackedTasksManager.createTask(task, file, dir, fileBackedTasksManager);
        assertEquals(Status.NEW.toString(), fileBackedTasksManager.getTaskStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), fileBackedTasksManager.getTaskStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), fileBackedTasksManager.getTaskStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
    }

    @Test
    public void getEpicStatusById() throws IOException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        assertEquals(Status.NEW.toString(), fileBackedTasksManager.getEpicStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE.toString(), fileBackedTasksManager.getEpicStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS.toString(), fileBackedTasksManager.getEpicStatusById(1, file, dir, fileBackedTasksManager), "Метод не работает");
    }

}