package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    ArrayList<Task> getPrioritizedTasksList();

    HashMap<Integer, Task> getTasksList();

    HashMap<Integer, Epic> getEpicsList();

    HistoryManager getInMemoryHistoryManager();

    HashMap<Integer, Task> createTask(Task task, String startTime, int duration);

    HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration);

    HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration);

    HashMap<Integer, Task> clearAllTasks();

    HashMap<Integer, Epic> clearAllEpic();

    HashMap<Integer, Subtask> clearAllSubTasks(int idNumber);

    Task getAnyTaskById(int idNumber);

    Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber);

    HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber);

    HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber);

    HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber);

    HashMap<Integer, Task> clearTaskById(int idNumber);

    HashMap<Integer, Epic> clearEpicById(int idNumber);

    HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int idNumber);

    String getTaskStatusById(int idNumber);

    String getEpicStatusById(int idNumber);

}
