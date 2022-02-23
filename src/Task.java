public class Task {
    private String name;
    private String description;
    private String status;

    public Task() {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
