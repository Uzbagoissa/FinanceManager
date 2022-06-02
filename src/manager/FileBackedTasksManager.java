package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager, Serializable {
    String dir;
    File file;

    public FileBackedTasksManager(File file, String dir) {
        this.file = file;
        this.dir = dir;
    }

    public FileBackedTasksManager() {

    }

    public HashMap<Integer, Task> getTasksList() {
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> getEpicsList() {
        return super.getEpicsList();
    }

    public HashMap<Integer, Task> createTask(Task task, String startTime, int duration) {
        super.createTask(task, startTime, duration);
        save();
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration) {
        super.createEpic(epic, startTime, duration);
        save();
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration) {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        super.createSubTask(epic, subtask, startTime, duration);
        save();
        return subTaskList;
    }

    public HashMap<Integer, Task> clearAllTasks() {
        super.clearAllTasks();
        save();
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearAllEpic() {
        super.clearAllEpic();
        save();
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearAllSubTasks(int idNumber) {
        HashMap<Integer, Subtask> subTaskList = super.clearAllSubTasks(idNumber);
        save();
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber) {
        Task task = super.getAnyTaskById(idNumber);
        save();
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber){
        Subtask subtask = super.getSubTaskById(epicIdNumber, subtaskIdNumber);
        save();
        return subtask;
    }

    public HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber) {
        super.renewTaskById(newTask, idNumber);
        save();
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber) {
        super.renewEpicById(newEpic, idNumber);
        save();
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
        HashMap<Integer, Subtask> subTaskList = super.renewSubTaskById(epic, newSubTask, idNumber);
        save();
        return subTaskList;
    }

    public HashMap<Integer, Task> clearTaskById(int idNumber) {
        super.clearTaskById(idNumber);
        save();
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearEpicById(int idNumber) {
        super.clearEpicById(idNumber);
        save();
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int subIdNumber) {
        HashMap<Integer, Subtask> subTaskList = super.clearSubTaskById(epic, subIdNumber);
        save();
        return subTaskList;
    }

    public String getTaskStatusById(int idNumber) {
        return super.getTaskStatusById(idNumber);
    }

    public String getEpicStatusById(int idNumber) {
        return super.getEpicStatusById(idNumber);
    }

    public void changeEpicStatus(Epic epic) {
        super.changeEpicStatus(epic);
    }

    public void changeEpicTime(Epic epic, Subtask subtask) {
        super.changeEpicTime(epic, subtask);
    }

    public static FileBackedTasksManager load(File file, String dir) throws ClassNotFoundException {
        FileBackedTasksManager fileBackedTasksManager = null;
        if (!file.exists()) {
            fileBackedTasksManager = new FileBackedTasksManager(file, dir);
        }
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                fileBackedTasksManager = (FileBackedTasksManager) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileBackedTasksManager;
    }

    public void save(){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (!file.exists()) {
                Files.createFile(Paths.get(dir, "backend.txt"));
            }
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert objectOutputStream != null;
                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}


