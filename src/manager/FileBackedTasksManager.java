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

    public ArrayList<Task> getPrioritizedTasksList() {
        return super.getPrioritizedTasksList();
    }

    public HashMap<Integer, Task> getTasksList() {
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> getEpicsList() {
        return super.getEpicsList();
    }

    public HistoryManager getInMemoryHistoryManager() {
        return super.getInMemoryHistoryManager();
    }

    public HashMap<Integer, Task> createTask(Task task, String startTime, int duration,  FileBackedTasksManager fileBackedTasksManager) {
        super.createTask(task, startTime, duration);
        save(fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration,  FileBackedTasksManager fileBackedTasksManager) {
        super.createEpic(epic, startTime, duration);
        save(fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration,  FileBackedTasksManager fileBackedTasksManager) {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        super.createSubTask(epic, subtask, startTime, duration);
        save(fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap<Integer, Task> clearAllTasks( FileBackedTasksManager fileBackedTasksManager) {
        super.clearAllTasks();
        save(fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearAllEpic( FileBackedTasksManager fileBackedTasksManager) {
        super.clearAllEpic();
        save(fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearAllSubTasks(int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        HashMap<Integer, Subtask> subTaskList = super.clearAllSubTasks(idNumber);
        save(fileBackedTasksManager);
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        Task task = super.getAnyTaskById(idNumber);
        save(fileBackedTasksManager);
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber,  FileBackedTasksManager fileBackedTasksManager){
        Subtask subtask = super.getSubTaskById(epicIdNumber, subtaskIdNumber);
        save(fileBackedTasksManager);
        return subtask;
    }

    public HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        super.renewTaskById(newTask, idNumber);
        save(fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        super.renewEpicById(newEpic, idNumber);
        save(fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        HashMap<Integer, Subtask> subTaskList = super.renewSubTaskById(epic, newSubTask, idNumber);
        save(fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap<Integer, Task> clearTaskById(int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        super.clearTaskById(idNumber);
        save(fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearEpicById(int idNumber,  FileBackedTasksManager fileBackedTasksManager) {
        super.clearEpicById(idNumber);
        save(fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int subIdNumber,  FileBackedTasksManager fileBackedTasksManager) {
        HashMap<Integer, Subtask> subTaskList = super.clearSubTaskById(epic, subIdNumber);
        save(fileBackedTasksManager);
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

    public static FileBackedTasksManager loadFromFile(File file, String dir) throws ClassNotFoundException {
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

    public void save(FileBackedTasksManager fileBackedTasksManager){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (!file.exists()) {
                Files.createFile(Paths.get(dir, "backend.txt"));
            }
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(fileBackedTasksManager);
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


