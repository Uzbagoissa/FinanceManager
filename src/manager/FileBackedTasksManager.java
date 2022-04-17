package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager, Serializable {
    String dir;
    File file;
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private int idNumber;
    private HistoryManager inMemoryHistoryManager;

    public FileBackedTasksManager(File file, String dir) {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
        this.file = file;
        this.dir = dir;
    }

    public static FileBackedTasksManager loadFromFile(File file, String dir) throws IOException, ClassNotFoundException {
        FileBackedTasksManager fileBackedTasksManager = null;
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        if (!file.exists()) {
            fileBackedTasksManager = new FileBackedTasksManager(file, dir);
        }
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream("backend.txt");
                objectInputStream = new ObjectInputStream(fileInputStream);
                fileBackedTasksManager = (FileBackedTasksManager) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                objectInputStream.close();
                fileInputStream.close();
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
                fileOutputStream = new FileOutputStream("backend.txt");
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(fileBackedTasksManager);
            } else {
                fileOutputStream = new FileOutputStream("backend.txt");
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(fileBackedTasksManager);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            objectOutputStream.close();
            fileOutputStream.close();
        }
    }

    public HashMap getTasksList(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        return taskList;
    }

    public HashMap getEpicsList(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        return epicList;
    }

    public HistoryManager getInMemoryHistoryManager(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        return inMemoryHistoryManager;
    }

    public HashMap createTask(Task task, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        idNumber += 1;
        task.setId(idNumber);
        taskList.put(idNumber, task);
        task.setStatus(Status.NEW);
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap createEpic(Epic epic, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        epic.setStatus(Status.NEW);
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap createSubTask(Epic epic, Subtask subtask, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        idNumber += 1;
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        subtask.setId(idNumber);
        subTaskList.put(idNumber, subtask);
        subtask.setStatus(Status.NEW);
        changeEpicStatus(epic);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap clearAllTasks(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        for (Integer idNumber : taskList.keySet()) {
            inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        }
        taskList.clear();
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap clearAllEpic(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        for (Integer idNumber : epicList.keySet()) {
            inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        }
        epicList.clear();
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap clearAllSubTasks(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Epic epic = epicList.get(idNumber);
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        for (Integer subtaskIdNumber : subTaskList.keySet()) {
            inMemoryHistoryManager.remove(getSubTaskById(idNumber, subtaskIdNumber));
        }
        subTaskList.clear();
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Task task = null;
        if (taskList.get(idNumber) != null) {
            task = taskList.get(idNumber);
            inMemoryHistoryManager.add(task);
        } else if (epicList.get(idNumber) != null) {
            task = epicList.get(idNumber);
            inMemoryHistoryManager.add(task);
        }
        save(file, dir, fileBackedTasksManager);
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException{
        Subtask task = null;
        Epic epic = epicList.get(epicIdNumber);
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        if (subTaskList.get(subtaskIdNumber) != null) {
            task = subTaskList.get(subtaskIdNumber);
            inMemoryHistoryManager.add(task);
        }
        save(file, dir, fileBackedTasksManager);
        return task;
    }

    public HashMap renewTaskById(Task newTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        newTask.setStatus(Status.NEW);
        newTask.setId(idNumber);
        taskList.put(idNumber, newTask);
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap renewEpicById(Epic newEpic, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        newEpic.setStatus(Status.NEW);
        newEpic.setId(idNumber);
        epicList.put(idNumber, newEpic);
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        newSubTask.setStatus(Status.NEW);
        newSubTask.setId(idNumber);
        subTaskList.put(idNumber, newSubTask);
        changeEpicStatus(epic);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public HashMap clearTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        taskList.remove(idNumber);
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap clearEpicById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        inMemoryHistoryManager.remove(getAnyTaskById(idNumber));
        epicList.remove(idNumber);
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap clearSubTaskById(Epic epic, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        inMemoryHistoryManager.remove(getSubTaskById(epic.getId(), idNumber));
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        subTaskList.remove(idNumber);
        changeEpicStatus(epic);
        save(file, dir, fileBackedTasksManager);
        return subTaskList;
    }

    public String getTaskStatusById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Task task = taskList.get(idNumber);
        return String.valueOf(task.getStatus());
    }

    public String getEpicStatusById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Epic epic = epicList.get(idNumber);
        return String.valueOf(epic.getStatus());
    }

    public void changeEpicStatus(Epic epic) {
        ArrayList<Status> statuses = new ArrayList<>();
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        for (Subtask task : subTaskList.values()) {
            Subtask subtask = task;
            statuses.add(subtask.getStatus());
        }
        if (subTaskList.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (!statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epic.setStatus(Status.DONE);
        } else if (statuses.contains(Status.NEW) && !statuses.contains(Status.DONE)) {
            epic.setStatus(Status.NEW);
        } else if (statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
