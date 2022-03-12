package model;

import manager.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subTaskList;
    private int idNumber;
    InMemoryHistoryManager inMemoryHistoryManager;

    public Epic() {
        super();
        this.subTaskList = new HashMap<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    public HashMap getSubTasksList() {
        return subTaskList;
    }

    public HashMap createSubTask(Epic epic, Subtask subtask) {
        idNumber += 1;
        subTaskList.put(idNumber, subtask);
        subtask.setStatus(Status.NEW);
        changeEpicStatus(epic);
        return subTaskList;
    }

    public HashMap clearAllSubTasks() {
        subTaskList.clear();
        return subTaskList;
    }

    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
        newSubTask.setStatus(Status.NEW);
        subTaskList.put(idNumber, newSubTask);
        changeEpicStatus(epic);
        return subTaskList;
    }

    public HashMap clearSubTaskById(Epic epic, int idNumber) {
        subTaskList.remove(idNumber);
        changeEpicStatus(epic);
        return subTaskList;
    }

    public void changeEpicStatus(Epic epic) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask task : subTaskList.values()) {
            Subtask subtask = task;
            statuses.add(subtask.getStatus());
        }
        if (subTaskList.isEmpty() == true) {
            epic.setStatus(Status.NEW);
        } else if (statuses.contains(Status.NEW) == false && statuses.contains(Status.DONE) == true && subTaskList.isEmpty() == false) {
            epic.setStatus(Status.DONE);
        } else if (statuses.contains(Status.NEW) == true && statuses.contains(Status.DONE) == false && subTaskList.isEmpty() == false) {
            epic.setStatus(Status.NEW);
        } else if (statuses.contains(Status.NEW) && statuses.contains(Status.DONE) && subTaskList.isEmpty() == false) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public Task getSubTaskById(int idNumber) {
        Task task = null;
        if (subTaskList.get(idNumber) != null) {
            task = subTaskList.get(idNumber);
        }
        inMemoryHistoryManager.add(task);
        return task;
    }

}
