package model;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subTaskList;

    public Epic() {
        super();
        this.subTaskList = new HashMap<>();
    }

    public HashMap getSubTasksList() {
        return subTaskList;
    }
}
