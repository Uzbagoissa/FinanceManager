package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subTaskList;
    private int idNumber;

    public Epic() {
        super();
        this.subTaskList = new HashMap<>();
        this.idNumber = 0;
    }

    public HashMap createSubTask(Epic epic, Subtask subtask) {
        idNumber += 1;
        subTaskList.put(idNumber, subtask);
        changeEpicStatus(epic);
        return subTaskList;
    }

    public Object getSubTasksList() {
        return subTaskList;
    }

    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber, String status) {
        newSubTask.setStatus(status);
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
        ArrayList<String> statuses = new ArrayList<>();
        for (Subtask task : subTaskList.values()) {
            Subtask subtask = task;
            statuses.add(subtask.getStatus());
        }
        if (subTaskList.isEmpty() == true) {
            epic.setStatus("NEW");
        } else if (statuses.contains("NEW") == false && statuses.contains("DONE") == true && subTaskList.isEmpty() == false) {
            epic.setStatus("DONE");
        } else if (statuses.contains("NEW") == true && statuses.contains("DONE") == false && subTaskList.isEmpty() == false) {
            epic.setStatus("NEW");
        } else if (statuses.contains("NEW") && statuses.contains("DONE") && subTaskList.isEmpty() == false) {
            epic.setStatus("IN_PROGRESS");
        }
    }

}
