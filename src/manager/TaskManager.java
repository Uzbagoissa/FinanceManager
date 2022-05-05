package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    void changeEpicTime(Epic epic, Subtask subtask);

    void changeEpicStatus(Epic epic);

    ArrayList getPrioritizedTasksList();

    HashMap getTasksList();

    HashMap getEpicsList();

    HistoryManager getInMemoryHistoryManager();

    HashMap createTask(Task task, String startTime, int duration);

    HashMap createEpic(Epic epic);

    HashMap createSubTask(Epic epic, Subtask subtask, String startTime, int duration);

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
