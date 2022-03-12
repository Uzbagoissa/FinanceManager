package manager;

import model.Epic;
import model.Task;

import java.util.HashMap;

public interface TaskManager {

    HashMap createTask(Task task);

    HashMap createEpic(Epic epic);

    HashMap clearAllTasks();

    HashMap clearAllEpic();

    Task getAnyTaskById(int idNumber);

    HashMap renewTaskById(Task newTask, int idNumber);

    HashMap renewEpicById(Epic newEpic, int idNumber);

    HashMap clearTaskById(int idNumber);

    HashMap clearEpicById(int idNumber);

    String getTaskStatusById(int idNumber);

    String getEpicStatusById(int idNumber);

}
