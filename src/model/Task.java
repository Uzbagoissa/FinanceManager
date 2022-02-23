package model;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private String status;
    private int idNumber;

    public Task() {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.idNumber = 0;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idNumber == task.idNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }
}
