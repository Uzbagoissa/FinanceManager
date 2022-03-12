package manager;

import model.Epic;
import model.Status;
import model.Task;

import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private int idNumber;
    HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    public HashMap getTasksList() {
        return taskList;
    }

    public HashMap getEpicsList() {
        return epicList;
    }

    @Override
    public HashMap createTask(Task task) {
        idNumber += 1;
        taskList.put(idNumber, task);
        task.setStatus(Status.NEW);
        return taskList;
    }

    @Override
    public HashMap createEpic(Epic epic) {
        idNumber += 1;
        epicList.put(idNumber, epic);
        epic.setStatus(Status.NEW);
        return epicList;
    }

    @Override
    public HashMap clearAllTasks() {
        taskList.clear();
        return taskList;
    }

    @Override
    public HashMap clearAllEpic() {
        epicList.clear();
        return epicList;
    }

    @Override
    public Task getAnyTaskById(int idNumber) {
        Task task = null;
        if (taskList.get(idNumber) != null) {
            task = taskList.get(idNumber);
        } else if (epicList.get(idNumber) != null) {
            task = epicList.get(idNumber);
        }
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public HashMap renewTaskById(Task newTask, int idNumber) {
        newTask.setStatus(Status.NEW);
        taskList.put(idNumber, newTask);
        return taskList;
    }

    @Override
    public HashMap renewEpicById(Epic newEpic, int idNumber) {
        newEpic.setStatus(Status.NEW);
        epicList.put(idNumber, newEpic);
        return epicList;
    }

    @Override
    public HashMap clearTaskById(int idNumber) {
        taskList.remove(idNumber);
        return taskList;
    }

    @Override
    public HashMap clearEpicById(int idNumber) {
        epicList.remove(idNumber);
        return epicList;
    }

    @Override
    public String getTaskStatusById(int idNumber) {
        Task task = taskList.get(idNumber);
        return String.valueOf(task.getStatus());
    }

    @Override
    public String getEpicStatusById(int idNumber) {
        Epic epic = epicList.get(idNumber);
        return String.valueOf(epic.getStatus());
    }

}