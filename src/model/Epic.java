package model;

import java.io.Serializable;
import java.util.HashMap;

public class Epic extends Task implements Serializable {
    private HashMap<Integer, Subtask> subTaskList;

    public Epic() {
        super();
        this.subTaskList = new HashMap<>();
    }

    public HashMap getSubTasksList() {
        return subTaskList;
    }

    @Override
    public String toString() {
        return getId() + ", " + Name.Epic + ", " + getStatus() + ", " + getDescription();
    }
}
