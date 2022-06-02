package test;

import manager.FileBackedTasksManager;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest {   //здесь тестируются методы класса FileBackedTasksManager,


    FileBackedTasksManager fileBackedTasksManager;
    String dir = System.getProperty("user.dir");
    File file = new File("backend.txt");

    @BeforeEach
    public void beforeEach() {
        fileBackedTasksManager = new FileBackedTasksManager(file, dir);
    }

    @Test
    public void saveAndLoadWithoutTasks() throws ClassNotFoundException {
        fileBackedTasksManager.save();
        FileBackedTasksManager.load(file, dir);
        assertTrue(fileBackedTasksManager.getTasksList().isEmpty(), "Сохранения состояния не произошло");
    }

    @Test
    public void saveAndLoadWithEpicWithoutSubtasks() throws ClassNotFoundException {
        Epic epic = new Epic();
        String startTime = "2000-01-01T01:00:00.000000000";
        int duration = 3;
        fileBackedTasksManager.createEpic(epic, startTime, duration);
        fileBackedTasksManager.save();
        FileBackedTasksManager.load(file, dir);
        assertEquals(epic, fileBackedTasksManager.getAnyTaskById(1), "Сохранения состояния не произошло");
    }

    @Test
    public void saveAndLoadWithEmptyHistoryList() throws ClassNotFoundException {
        fileBackedTasksManager.save();
        FileBackedTasksManager.load(file, dir);
        assertTrue(fileBackedTasksManager.getInMemoryHistoryManager().getHistory().isEmpty(), "Сохранения состояния не произошло");
    }

}
