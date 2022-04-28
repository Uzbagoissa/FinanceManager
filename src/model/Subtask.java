package model;

import java.io.Serializable;

public class Subtask extends Task implements Serializable {

    public Subtask() {
        super();
    }

    @Override
    public String toString() {
        return getId() + ", " + Name.SUBTASK + ", " + getStatus() + ", " + getDescription() + ", " + getStartTime() + ", " + getEndTime() + ", " + getDuration() + "\n";
    }
}
