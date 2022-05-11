package model;

import java.io.Serializable;
import java.util.HashMap;

public class Epic extends Task implements Serializable {
    private final HashMap<Integer, Subtask> subTaskList;

    public Epic() {
        super();
        this.subTaskList = new HashMap<>();
    }

    public HashMap<Integer, Subtask> getSubTasksList() {
        return subTaskList;
    }

    @Override
    public String toString() {
        return getId() + ", " + Name.EPIC + ", " + getStatus() + ", " + getDescription() + ", " + getStartTime() + ", " + getEndTime() + ", " + getDuration() + "\n";
    }
}
