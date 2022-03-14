package model;

public class Task {
    private String name;
    private String description;
    private Status status;

    public Task() {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
