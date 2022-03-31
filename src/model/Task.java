package model;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private Name name;
    private String description;
    private Status status;

    public Task() {
        this.name = name;
        this.description = "Description";
        this.status = status;
    }

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

    public void setName(String name) {
        this.name = Name.Task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + ", " + Name.Task + ", " + status + ", " + description;
    }
}
