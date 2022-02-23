import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private int idNumber;

    public Manager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.idNumber = 0;
    }

    public HashMap createTask(Task task) {
        idNumber += 1;
        taskList.put(idNumber, task);
        return taskList;
    }

    public HashMap createEpic(Epic epic) {
        idNumber += 1;
        epicList.put(idNumber, epic);
        return epicList;
    }

    public HashMap getTasksList() {
        return taskList;
    }

    public HashMap getEpicsList() {
        return epicList;
    }

    public HashMap clearAllTasks() {
        taskList.clear();
        return taskList;
    }

    public HashMap clearAllEpic() {
        epicList.clear();
        return epicList;
    }

    public Object getAnyTaskById(int idNumber) {
        Object o = null;
        if (taskList.get(idNumber) != null) {
            o = taskList.get(idNumber);
        } else if (epicList.get(idNumber) != null) {
            o = epicList.get(idNumber);
        }
        return o;
    }

    public HashMap renewTaskById(Task newTask, int idNumber, String status) {
        newTask.setStatus(status);
        taskList.put(idNumber, newTask);
        return taskList;
    }

    public HashMap renewEpicById(Epic newEpic, int idNumber, String status) {
        newEpic.setStatus(status);
        epicList.put(idNumber, newEpic);
        return epicList;
    }

    public HashMap clearTaskById(int idNumber) {
        taskList.remove(idNumber);
        return taskList;
    }

    public HashMap clearEpicById(int idNumber) {
        epicList.remove(idNumber);
        return epicList;
    }

    public String getTaskStatusById(int idNumber) {
        Task task = taskList.get(idNumber);
        return task.getStatus();
    }

    public String getEpicStatusById(int idNumber) {
        Epic epic = epicList.get(idNumber);
        return epic.getStatus();
    }

}