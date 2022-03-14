package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private int idNumber;
    private HistoryManager inMemoryHistoryManager;

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

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public HashMap createTask(Task task) {
        idNumber += 1;
        task.setId(idNumber);
        taskList.put(idNumber, task);
        task.setStatus(Status.NEW);
        return taskList;
    }

    @Override
    public HashMap createEpic(Epic epic) {
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        epic.setStatus(Status.NEW);
        return epicList;
    }

    @Override
    public HashMap createSubTask(Epic epic, Subtask subtask) {
        idNumber += 1;
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        subtask.setId(idNumber);
        subTaskList.put(idNumber, subtask);
        subtask.setStatus(Status.NEW);
        changeEpicStatus(epic);
        return subTaskList;
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
    public HashMap clearAllSubTasks(int idNumber) {
        Epic epic = epicList.get(idNumber);
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        subTaskList.clear();
        return subTaskList;
    }

    @Override
    public Task getAnyTaskById(int idNumber) {
        Task task = null;
        if (taskList.get(idNumber) != null) {
            task = taskList.get(idNumber);
            inMemoryHistoryManager.add(task);
        } else if (epicList.get(idNumber) != null) {
            task = epicList.get(idNumber);
            inMemoryHistoryManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber) {
        Epic epic = epicList.get(epicIdNumber);
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        Subtask task = null;
        if (subTaskList.get(subtaskIdNumber) != null) {
            task = subTaskList.get(subtaskIdNumber);
            inMemoryHistoryManager.add(task);
        }
        return task;
    }

    @Override
    public HashMap renewTaskById(Task newTask, int idNumber) {
        newTask.setStatus(Status.NEW);
        newTask.setId(idNumber);
        taskList.put(idNumber, newTask);
        return taskList;
    }

    @Override
    public HashMap renewEpicById(Epic newEpic, int idNumber) {
        newEpic.setStatus(Status.NEW);
        newEpic.setId(idNumber);
        epicList.put(idNumber, newEpic);
        return epicList;
    }

    @Override
    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        newSubTask.setStatus(Status.NEW);
        newSubTask.setId(idNumber);
        subTaskList.put(idNumber, newSubTask);
        changeEpicStatus(epic);
        return subTaskList;
    }

    @Override
    public HashMap clearTaskById(int idNumber) {
        inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        taskList.remove(idNumber);
        return taskList;
    }

    @Override
    public HashMap clearEpicById(int idNumber) {
        inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        epicList.remove(idNumber);
        return epicList;
    }

    @Override
    public HashMap clearSubTaskById(Epic epic, int idNumber) {
        inMemoryHistoryManager.remove(getSubTaskById(epic.getId(), idNumber));
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        subTaskList.remove(idNumber);
        changeEpicStatus(epic);
        return subTaskList;
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

    public void changeEpicStatus(Epic epic) {
        ArrayList<Status> statuses = new ArrayList<>();
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        for (Subtask task : subTaskList.values()) {
            Subtask subtask = task;
            statuses.add(subtask.getStatus());
        }
        if (subTaskList.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (!statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epic.setStatus(Status.DONE);
        } else if (statuses.contains(Status.NEW) && !statuses.contains(Status.DONE)) {
            epic.setStatus(Status.NEW);
        } else if (statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}