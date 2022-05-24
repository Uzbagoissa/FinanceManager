package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager, Serializable {
    KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient(url);
    }

    public ArrayList<Task> getPrioritizedTasksList() {
        return super.getPrioritizedTasksList();
    }

    public HashMap<Integer, Task> getTasksList() {
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> getEpicsList() {
        return super.getEpicsList();
    }

    public HistoryManager getInMemoryHistoryManager() {
        return super.getInMemoryHistoryManager();
    }

    public HashMap<Integer, Task> createTask(Task task, String startTime, int duration) {
        String key = "taskList";
        save(key, super.createTask(task, startTime, duration));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration) {
        String key = "epicList";
        save(key, super.createEpic(epic, startTime, duration));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration) {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        String key = "subtaskList" + epic.getId();
        save(key, super.createSubTask(epic, subtask, startTime, duration));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return subTaskList;
    }

    public HashMap<Integer, Task> clearAllTasks() {
        String key = "taskList";
        save(key, super.clearAllTasks());
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearAllEpic() {
        String key = "epicList";
        save(key, super.clearAllEpic());
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearAllSubTasks(int idNumber) {
        HashMap<Integer, Subtask> subTaskList = super.clearAllSubTasks(idNumber);
        String key = "subtaskList" + idNumber;
        save(key, subTaskList);
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber) {
        Task task = super.getAnyTaskById(idNumber);
        String key = "history";
        save(key, getInMemoryHistoryManager().getHistory());
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber){
        Subtask subtask = super.getSubTaskById(epicIdNumber, subtaskIdNumber);
        String key = "history";
        save(key, getInMemoryHistoryManager().getHistory());
        return subtask;
    }

    public HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber) {
        String key = "taskList";
        save(key, super.renewTaskById(newTask, idNumber));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber) {
        String key = "epicList";
        save(key, super.renewEpicById(newEpic, idNumber));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
        HashMap<Integer, Subtask> subTaskList = super.renewSubTaskById(epic, newSubTask, idNumber);
        String key = "subtaskList" + epic.getId();
        save(key, subTaskList);
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        return subTaskList;
    }

    public HashMap<Integer, Task> clearTaskById(int idNumber) {
        String key = "taskList";
        save(key, super.clearTaskById(idNumber));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearEpicById(int idNumber) {
        String key = "epicList";
        save(key, super.clearEpicById(idNumber));
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int subIdNumber) {
        HashMap<Integer, Subtask> subTaskList = super.clearSubTaskById(epic, subIdNumber);
        String key = "subtaskList" + epic.getId();
        save(key, subTaskList);
        String key1 = "prioritizedTasksList";
        save(key1, super.getPrioritizedTasksList());
        String key2 = "history";
        save(key2, getInMemoryHistoryManager().getHistory());
        return subTaskList;
    }

    public String getTaskStatusById(int idNumber) {
        return super.getTaskStatusById(idNumber);
    }

    public String getEpicStatusById(int idNumber) {
        return super.getEpicStatusById(idNumber);
    }

    public void changeEpicStatus(Epic epic) {
        super.changeEpicStatus(epic);
    }

    public void changeEpicTime(Epic epic, Subtask subtask) {
        super.changeEpicTime(epic, subtask);
    }

    public String loadFromServer(String key){
        return kvTaskClient.load(key);
    }

    public void save(String key, Object o){
        String value = o.toString();
        kvTaskClient.put(key, value);
    }
}
