package model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private int id;
    private Name name;
    private String description = "Description";
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return id + ", " + Name.TASK + ", " + status + ", " + description + ", " + startTime + ", " + endTime + ", " + duration + "\n";
    }
}
