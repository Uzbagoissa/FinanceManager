package model;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private Name name;
    private String description = "Description";
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Name getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return id + ", " + Name.TASK + ", " + status + ", " + description;
    }
}
