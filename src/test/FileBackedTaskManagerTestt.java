package test;

import manager.FileBackedTasksManager;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTestt extends TaskManagerTest {   //здесь тестируются методы класса FileBackedTasksManager,


    FileBackedTasksManager fileBackedTasksManager;
    String dir = System.getProperty("user.dir");
    File file = new File("backend.txt");

    @BeforeEach
    public void beforeEach() {
        fileBackedTasksManager = new FileBackedTasksManager(file, dir);
    }

    @Test
    public void saveAndLoadWithoutTasks() throws IOException, ClassNotFoundException {
        fileBackedTasksManager.save(file, dir, fileBackedTasksManager);
        FileBackedTasksManager.loadFromFile(file, dir);
        assertTrue(fileBackedTasksManager.getTasksList().isEmpty(), "Сохранения состояния не произошло");
    }

    @Test
    public void saveAndLoadWithEpicWithoutSubtasks() throws IOException, ClassNotFoundException {
        Epic epic = new Epic();
        fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager);
        fileBackedTasksManager.save(file, dir, fileBackedTasksManager);
        FileBackedTasksManager.loadFromFile(file, dir);
        assertEquals(epic, fileBackedTasksManager.getAnyTaskById(1, file, dir, fileBackedTasksManager), "Сохранения состояния не произошло");
    }

    @Test
    public void saveAndLoadWithEmptyHistoryList() throws IOException, ClassNotFoundException {
        fileBackedTasksManager.save(file, dir, fileBackedTasksManager);
        FileBackedTasksManager.loadFromFile(file, dir);
        assertTrue(fileBackedTasksManager.getInMemoryHistoryManager().getHistory().isEmpty(), "Сохранения состояния не произошло");
    }

}
