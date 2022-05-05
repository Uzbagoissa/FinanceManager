package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager, Serializable {
    String dir;
    File file;
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private ArrayList<Task> prioritizedTasksList;
    private int idNumber;
    private HistoryManager inMemoryHistoryManager;
    TaskComparator taskComparator = new TaskComparator();

    public FileBackedTasksManager(File file, String dir) {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.prioritizedTasksList = new ArrayList<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
        this.file = file;
        this.dir = dir;
    }

    public ArrayList getPrioritizedTasksList(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        prioritizedTasksList.sort(taskComparator);
        return prioritizedTasksList;
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

    public HashMap createTask(Task task, String startTime, int duration, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            LocalDateTime taskStartTime = LocalDateTime.parse(startTime);
            for (Task taskk : prioritizedTasksList) {
                if ((taskStartTime.isAfter(taskk.getStartTime()) && taskStartTime.isBefore(taskk.getEndTime())) || taskStartTime.isEqual(taskk.getStartTime()) || taskStartTime.isEqual(taskk.getEndTime())){
                    System.out.println("Время занято выполнением другой задачи. Выберите другое время. " + "Позже, чем " + taskk.getEndTime() + "\n" + " или раньше, чем " + taskk.getStartTime());
                    return taskList;
                }
            }
            LocalDateTime taskFinishTime = taskStartTime.plusHours(duration);
            if ((taskFinishTime.isBefore(taskStartTime)) || taskFinishTime.isEqual(taskStartTime)){
                System.out.println("Время окончания задачи должно быть позже времени начала задачи");
                return taskList;
            }
            List<LocalDateTime> isAfterTaskStartTime = new ArrayList<>();
            for (Task taskk : prioritizedTasksList) {
                if ((taskk.getStartTime() != null) && (taskk.getStartTime().isAfter(taskStartTime))){
                    isAfterTaskStartTime.add(taskk.getStartTime());
                }
            }
            LocalDateTime minStartTaskTime = LocalDateTime.parse("2200-01-01T01:00:00.000000000");
            for (LocalDateTime localDateTime : isAfterTaskStartTime) {
                if (localDateTime.isBefore(minStartTaskTime)) {
                    minStartTaskTime = localDateTime;
                }
            }
            if ((taskFinishTime.isAfter(minStartTaskTime)) || taskFinishTime.isEqual(minStartTaskTime)){
                System.out.println("Выберите более раннее время окончания задачи, чем " + minStartTaskTime);
                return taskList;
            }
            idNumber += 1;
            task.setId(idNumber);
            taskList.put(idNumber, task);
            taskList.get(idNumber).setStartTime(taskStartTime);
            Duration taskDuration = Duration.ofHours(duration);
            taskList.get(idNumber).setDuration(taskDuration);
            taskList.get(idNumber).setEndTime(taskFinishTime);
            prioritizedTasksList.add(task);
            task.setStatus(Status.NEW);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return taskList;
    }

    public HashMap createEpic(Epic epic, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        LocalDateTime epicStartTime = LocalDateTime.parse("2200-01-01T01:00:00.000000000");
        Duration epicDuration = Duration.ofHours(0);
        LocalDateTime epicFinishTime = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        epicList.get(idNumber).setStartTime(epicStartTime);
        epicList.get(idNumber).setDuration(epicDuration);
        epicList.get(idNumber).setEndTime(epicFinishTime);
        prioritizedTasksList.add(epic);
        epic.setStatus(Status.NEW);
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap createSubTask(Epic epic, Subtask subtask, String startTime, int duration, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
        try {
            LocalDateTime epicStartTime = LocalDateTime.parse(startTime);
            for (Task taskk : prioritizedTasksList) {
                if ((epicStartTime.isAfter(taskk.getStartTime()) && epicStartTime.isBefore(taskk.getEndTime())) || epicStartTime.isEqual(taskk.getStartTime()) || epicStartTime.isEqual(taskk.getEndTime())){
                    System.out.println("Время занято выполнением другой задачи. Выберите другое время. " + "Позже, чем " + taskk.getEndTime() + "\n" + " или раньше, чем " + taskk.getStartTime());
                    return subTaskList;
                }
            }
            LocalDateTime subTaskFinishTime = epicStartTime.plusHours(duration);
            if ((subTaskFinishTime.isBefore(epicStartTime)) || subTaskFinishTime.isEqual(epicStartTime)){
                System.out.println("Время окончания задачи должно быть позже времени начала задачи");
                return subTaskList;
            }
            List<LocalDateTime> isAfterTaskStartTime = new ArrayList<>();
            for (Task taskk : prioritizedTasksList) {
                if ((taskk.getStartTime() != null) && (taskk.getStartTime().isAfter(epicStartTime))){
                    isAfterTaskStartTime.add(taskk.getStartTime());
                }
            }
            LocalDateTime minStartTaskTime = LocalDateTime.parse("2200-01-01T01:00:00.000000000");
            for (LocalDateTime localDateTime : isAfterTaskStartTime) {
                if (localDateTime.isBefore(minStartTaskTime)) {
                    minStartTaskTime = localDateTime;
                }
            }
            if ((subTaskFinishTime.isAfter(minStartTaskTime)) || subTaskFinishTime.isEqual(minStartTaskTime)){
                System.out.println("Выберите более раннее время окончания задачи, чем " + minStartTaskTime);
                return subTaskList;
            }
            idNumber += 1;
            subtask.setId(idNumber);
            subTaskList.put(idNumber, subtask);
            subtask.setStartTime(epicStartTime);
            Duration subTaskDuration = Duration.ofHours(duration);
            subtask.setDuration(subTaskDuration);
            subtask.setEndTime(subTaskFinishTime);
            prioritizedTasksList.add(subtask);
            subtask.setStatus(Status.NEW);
            changeEpicTime(epic, subtask);
            changeEpicStatus(epic);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    public HashMap clearAllTasks(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        for (Integer idNumber : taskList.keySet()) {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
        }
        taskList.clear();
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap clearAllEpic(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        for (Integer idNumber : epicList.keySet()) {
            inMemoryHistoryManager.remove(epicList.get(idNumber));
            HashMap<Integer, Subtask> subTaskList = epicList.get(idNumber).getSubTasksList();
            for (Subtask subtask : subTaskList.values()) {
                prioritizedTasksList.remove(subtask);
            }
            prioritizedTasksList.remove(epicList.get(idNumber));
        }
        epicList.clear();
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap clearAllSubTasks(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            Epic epic = epicList.get(idNumber);
            subTaskList = epic.getSubTasksList();
            for (Integer subtaskIdNumber : subTaskList.keySet()) {
                inMemoryHistoryManager.remove(subTaskList.get(subtaskIdNumber));
            }
            for (Subtask subtask : subTaskList.values()) {
                prioritizedTasksList.remove(subtask);
            }
            subTaskList.clear();
            LocalDateTime epicStartTime = LocalDateTime.parse("2200-01-01T01:00:00.000000000");
            Duration epicDuration = Duration.ofHours(0);
            LocalDateTime epicFinishTime = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
            epic.setStartTime(epicStartTime);
            epic.setDuration(epicDuration);
            epic.setEndTime(epicFinishTime);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    public Task getAnyTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        Task task = null;
        try {
            if (taskList.get(idNumber) != null) {
                task = taskList.get(idNumber);
                inMemoryHistoryManager.add(task);
            } else if (epicList.get(idNumber) != null) {
                task = epicList.get(idNumber);
                inMemoryHistoryManager.add(task);
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return task;
    }

    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException{
        Subtask subtask = null;
        try {
            Epic epic = epicList.get(epicIdNumber);
            HashMap<Integer, Subtask> subTaskList = epic.getSubTasksList();
            if (subTaskList.get(subtaskIdNumber) != null) {
                subtask = subTaskList.get(subtaskIdNumber);
                inMemoryHistoryManager.add(subtask);
            } else {
                System.out.println("Такой Subtask задачи в списке нет");
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return subtask;
    }

    public HashMap renewTaskById(Task newTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        prioritizedTasksList.remove(taskList.get(idNumber));
        newTask.setStatus(Status.NEW);
        newTask.setId(idNumber);
        taskList.put(idNumber, newTask);
        prioritizedTasksList.add(newTask);
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap renewEpicById(Epic newEpic, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        clearAllSubTasks(idNumber, file, dir, fileBackedTasksManager);
        prioritizedTasksList.remove(epicList.get(idNumber));
        newEpic.setStatus(Status.NEW);
        newEpic.setId(idNumber);
        epicList.put(idNumber, newEpic);
        prioritizedTasksList.add(epicList.get(idNumber));
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            subTaskList = epic.getSubTasksList();
            if (!subTaskList.containsKey(idNumber)) {
                System.out.println("Такой Subtask задачи в списке нет");
            } else {
                if (prioritizedTasksList.contains(subTaskList.get(idNumber))){
                    prioritizedTasksList.remove(subTaskList.get(idNumber));
                }
                newSubTask.setStatus(Status.NEW);
                newSubTask.setId(idNumber);
                subTaskList.put(idNumber, newSubTask);
                prioritizedTasksList.add(subTaskList.get(idNumber));
                changeEpicStatus(epic);
                save(file, dir, fileBackedTasksManager);
            }
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    public HashMap clearTaskById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
            taskList.remove(idNumber);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return taskList;
    }

    public HashMap clearEpicById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            inMemoryHistoryManager.remove(epicList.get(idNumber));
            prioritizedTasksList.remove(epicList.get(idNumber));
            clearAllSubTasks(idNumber, file, dir, fileBackedTasksManager);
            epicList.remove(idNumber);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return epicList;
    }

    public HashMap clearSubTaskById(Epic epic, int subIdNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            subTaskList = epic.getSubTasksList();
            inMemoryHistoryManager.remove(subTaskList.get(subIdNumber));
            prioritizedTasksList.remove(subTaskList.get(subIdNumber));
            subTaskList.remove(subIdNumber);
            changeEpicStatus(epic);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    public String getTaskStatusById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        String o = "";
        try {
            Task task = taskList.get(idNumber);
            o = String.valueOf(task.getStatus());
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return o;
    }

    public String getEpicStatusById(int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        String o = "";
        try {
            Epic epic = epicList.get(idNumber);
            o = String.valueOf(epic.getStatus());
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return o;
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
            }
            fileOutputStream = new FileOutputStream("backend.txt");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(fileBackedTasksManager);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            objectOutputStream.close();
            fileOutputStream.close();
        }
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
        } else if (!statuses.contains(Status.NEW) && statuses.contains(Status.DONE) && !statuses.contains(Status.IN_PROGRESS)) {
            epic.setStatus(Status.DONE);
        } else if (statuses.contains(Status.NEW) && !statuses.contains(Status.DONE) && !statuses.contains(Status.IN_PROGRESS)) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void changeEpicTime(Epic epic, Subtask subtask) {
        if (epic.getStartTime().isAfter(subtask.getStartTime())){
            LocalDateTime epicStartTime = subtask.getStartTime();
            epic.setStartTime(epicStartTime);
        }
        if (epic.getEndTime().isBefore(subtask.getEndTime())){
            LocalDateTime epicEndTime = subtask.getEndTime();
            epic.setEndTime(epicEndTime);
        }
        if (epic.getDuration() == Duration.ofHours(0)){
            Duration epicDuration = subtask.getDuration();
            epic.setDuration(epicDuration);
        } else if (epic.getDuration() != Duration.ofHours(0)){
            Duration epicDuration = Duration.between(epic.getStartTime(), subtask.getEndTime());
            epic.setDuration(epicDuration);
        }
    }

}


