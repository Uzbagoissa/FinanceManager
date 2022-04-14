package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;

public interface TaskManager {

    HashMap createTask(Task task);

    HashMap createEpic(Epic epic);

    HashMap createSubTask(Epic epic, Subtask subtask);

    HashMap clearAllTasks();

    HashMap clearAllEpic();

    HashMap clearAllSubTasks(int idNumber);

    Task getAnyTaskById(int idNumber);

    Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber);

    HashMap renewTaskById(Task newTask, int idNumber);

    HashMap renewEpicById(Epic newEpic, int idNumber);

    HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber);

    HashMap clearTaskById(int idNumber);

    HashMap clearEpicById(int idNumber);

    HashMap clearSubTaskById(Epic epic, int idNumber);

    String getTaskStatusById(int idNumber);

    String getEpicStatusById(int idNumber);
}
