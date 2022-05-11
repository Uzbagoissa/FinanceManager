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

    public HashMap<Integer, Task> createTask(Task task, String startTime, int duration, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.createTask(task, startTime, duration);
        save(file, dir, fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.createEpic(epic, startTime, duration);
        save(file, dir, fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        super.createSubTask(epic, subtask, startTime, duration);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap<Integer, Task> clearAllTasks(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.clearAllTasks();
        save(file, dir, fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearAllEpic(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.clearAllEpic();
        save(file, dir, fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearAllSubTasks(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = super.clearAllSubTasks(idNumber);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Task task = super.getAnyTaskById(idNumber);
        save(file, dir, fileBackedTasksManager);
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException{
        Subtask subtask = super.getSubTaskById(epicIdNumber, subtaskIdNumber);
        save(file, dir, fileBackedTasksManager);
        return subtask;
    }

    public HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.renewTaskById(newTask, idNumber);
        save(file, dir, fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.renewEpicById(newEpic, idNumber);
        save(file, dir, fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = super.renewSubTaskById(epic, newSubTask, idNumber);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap<Integer, Task> clearTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.clearTaskById(idNumber);
        save(file, dir, fileBackedTasksManager);
        return super.getTasksList();
    }

    public HashMap<Integer, Epic> clearEpicById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        super.clearEpicById(idNumber);
        save(file, dir, fileBackedTasksManager);
        return super.getEpicsList();
    }

    public HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int subIdNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = super.clearSubTaskById(epic, subIdNumber);
        save(file, dir, fileBackedTasksManager);
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
            try (FileInputStream fileInputStream = new FileInputStream("backend.txt"); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                fileBackedTasksManager = (FileBackedTasksManager) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileBackedTasksManager;
    }

    public void save(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException{
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (!file.exists()) {
                Files.createFile(Paths.get(dir, "backend.txt"));
            }
            fileOutputStream = new FileOutputStream("backend.txt");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(fileBackedTasksManager);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert objectOutputStream != null;
            objectOutputStream.close();
            fileOutputStream.close();
        }
    }

}


