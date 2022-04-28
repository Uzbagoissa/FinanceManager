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
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager, Serializable {
    String dir;
    File file;
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private ArrayList<Integer> taskInProgressList;
    private ArrayList<Task> prioritizedTasksList;
    private int idNumber;
    private HistoryManager inMemoryHistoryManager;
    TaskComparator taskComparator = new TaskComparator();

    public FileBackedTasksManager(File file, String dir) {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.taskInProgressList = new ArrayList<>();
        this.prioritizedTasksList = new ArrayList<>();          //TreeSet не осилил. Сделал с ArrayList
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

    public void startTask(int idNumber, String startTime, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            if (!taskInProgressList.isEmpty()){
                System.out.println("Приступить к выполнению новой задачи нельзя, пока выполняется другая задача. Для начала закончите эту задачу");
            } else {
                LocalDateTime taskStartTime;
                if (startTime == "now"){
                    taskStartTime = LocalDateTime.now();
                } else {
                    taskStartTime = LocalDateTime.parse(startTime);
                    for (Task task : prioritizedTasksList) {
                        LocalDateTime a = task.getStartTime();
                        if (a == null){
                            a = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
                        }
                        LocalDateTime b = task.getEndTime();
                        if (b == null){
                            b = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
                        }
                        if ((taskStartTime.isAfter(a) && taskStartTime.isBefore(b)) || taskStartTime.isEqual(a) || taskStartTime.isEqual(b)){
                            System.out.println("Время занято выполнением другой задачи. Выберите другое время. " + "Позже, чем " + b + "\n" + " или раньше, чем " + a);
                            return;
                        }
                    }
                }
                taskList.get(idNumber).setStartTime(taskStartTime);
                taskList.get(idNumber).setStatus(Status.IN_PROGRESS);
                taskInProgressList.add(idNumber);
                System.out.println("Приступили к выполнению задачи № " + idNumber);
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void startEpic(int idNumber, int subIdNumber, String startTime, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            if (!taskInProgressList.isEmpty()){
                System.out.println("Приступить к выполнению новой задачи нельзя, пока выполняется другая задача. Для начала закончите эту задачу");
            } else {
                HashMap<Integer, Subtask> subTaskList = epicList.get(idNumber).getSubTasksList();
                LocalDateTime epicStartTime;
                if (startTime == "now") {
                    epicStartTime = LocalDateTime.now();
                } else {
                    epicStartTime = LocalDateTime.parse(startTime);
                    for (Task task : prioritizedTasksList) {
                        LocalDateTime a = task.getStartTime();
                        if (a == null){
                            a = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
                        }
                        LocalDateTime b = task.getEndTime();
                        if (b == null){
                            b = LocalDateTime.parse("2000-01-01T01:00:00.000000000");
                        }
                        if ((epicStartTime.isAfter(a) && epicStartTime.isBefore(b)) || epicStartTime.isEqual(a) || epicStartTime.isEqual(b)){
                            System.out.println("Время занято выполнением другой задачи. Выберите другое время. " + "Позже, чем " + b + "\n" + " или раньше, чем " + a);
                            return;
                        }
                    }
                }
                subTaskList.get(subIdNumber).setStartTime(epicStartTime);
                if (epicList.get(idNumber).getStatus() == Status.NEW) {
                    changeEpicTime(epicList.get(idNumber), subTaskList.get(subIdNumber));
                }
                subTaskList.get(subIdNumber).setStatus(Status.IN_PROGRESS);
                changeEpicStatus(epicList.get(idNumber));
                taskInProgressList.add(subIdNumber);
                taskInProgressList.add(idNumber);
                System.out.println("Приступили к выполнению подзадачи № " + subIdNumber + " эпика № " + idNumber);
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void finishTask(int idNumber, String finishTime, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            if (taskList.get(idNumber).getStartTime() == null){
                System.out.println("Сначала задайте время старта задачи");
            }
            if (!taskInProgressList.contains(idNumber)){
                System.out.println("Эта задача не в работе");
            } else {
                LocalDateTime taskFinishTime;
                if (finishTime == "now"){
                    taskFinishTime = LocalDateTime.now();
                } else {
                    taskFinishTime = LocalDateTime.parse(finishTime);
                    if ((taskFinishTime.isBefore(taskList.get(idNumber).getStartTime())) || taskFinishTime.isEqual(taskList.get(idNumber).getStartTime())){
                        System.out.println("Время окончания задачи должно быть позже времени начала задачи");
                        return;
                    }
                    List<LocalDateTime> isAfterTaskStartTime = new ArrayList<>();
                    for (Task task : prioritizedTasksList) {
                        if ((task.getStartTime() != null) && (task.getStartTime().isAfter(taskList.get(idNumber).getStartTime()))){
                            isAfterTaskStartTime.add(task.getStartTime());
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
                        return;
                    }
                }
                taskList.get(idNumber).setEndTime(taskFinishTime);
                Duration taskDuration = Duration.between(taskList.get(idNumber).getStartTime(), taskFinishTime);
                taskList.get(idNumber).setDuration(taskDuration);
                taskList.get(idNumber).setStatus(Status.DONE);
                taskInProgressList.clear();
                System.out.println("Закончили выполнение задачи № " + idNumber);
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void finishEpic(int idNumber, int subIdNumber, String finishTime, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        try {
            HashMap<Integer, Subtask> subTaskList = epicList.get(idNumber).getSubTasksList();
            if (subTaskList.get(subIdNumber).getStartTime() == null){
                System.out.println("Сначала задайте время старта задачи");
            }
            if (!taskInProgressList.contains(subIdNumber)){
                System.out.println("Эта задача не в работе");
            } else {
                LocalDateTime subTaskFinishTime;
                if (finishTime == "now") {
                    subTaskFinishTime = LocalDateTime.now();
                } else {
                    subTaskFinishTime = LocalDateTime.parse(finishTime);
                    if ((subTaskFinishTime.isBefore(subTaskList.get(subIdNumber).getStartTime())) || subTaskFinishTime.isEqual(subTaskList.get(subIdNumber).getStartTime())){
                        System.out.println("Время окончания задачи должно быть позже времени начала задачи");
                        return;
                    }
                    List<LocalDateTime> isAfterTaskStartTime = new ArrayList<>();
                    for (Task task : prioritizedTasksList) {
                        if ((task.getStartTime() != null) && (task.getStartTime().isAfter(subTaskList.get(subIdNumber).getStartTime()))){
                            isAfterTaskStartTime.add(task.getStartTime());
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
                        return;
                    }
                }
                subTaskList.get(subIdNumber).setEndTime(subTaskFinishTime);
                Duration subTaskDuration = Duration.between(subTaskList.get(subIdNumber).getStartTime(), subTaskFinishTime);
                subTaskList.get(subIdNumber).setDuration(subTaskDuration);
                changeEpicTime(epicList.get(idNumber), subTaskList.get(subIdNumber));
                subTaskList.get(subIdNumber).setStatus(Status.DONE);
                changeEpicStatus(epicList.get(idNumber));
                taskInProgressList.clear();
                System.out.println("Закончили выполнение подзадачи № " + epicList.get(idNumber) + " эпика № " + idNumber);
            }
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
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
        if (epic.getStartTime() == null){
            LocalDateTime epicStartTime = subtask.getStartTime();
            epic.setStartTime(epicStartTime);
        }
        if (epic.getEndTime() == null){
            LocalDateTime epicEndTime = subtask.getEndTime();
            epic.setEndTime(epicEndTime);
        } else if (epic.getEndTime().isBefore(subtask.getEndTime())){
            LocalDateTime epicEndTime = subtask.getEndTime();
            epic.setEndTime(epicEndTime);
        }
        if (epic.getDuration() == null){
            Duration epicDuration = subtask.getDuration();
            epic.setDuration(epicDuration);
        } else if (epic.getDuration() != null){
            Duration epicDuration = Duration.between(epic.getStartTime(), subtask.getEndTime());
            epic.setDuration(epicDuration);
        }
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

    public HashMap createTask(Task task, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        idNumber += 1;
        task.setId(idNumber);
        taskList.put(idNumber, task);
        prioritizedTasksList.add(task);
        task.setStatus(Status.NEW);
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap createEpic(Epic epic, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        prioritizedTasksList.add(epic);
        epic.setStatus(Status.NEW);
        save(file, dir, fileBackedTasksManager);
        return epicList;
    }

    public HashMap createSubTask(Epic epic, Subtask subtask, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            idNumber += 1;
            subTaskList = epic.getSubTasksList();
            subtask.setId(idNumber);
            subTaskList.put(idNumber, subtask);
            prioritizedTasksList.add(subtask);
            subtask.setStatus(Status.NEW);
            changeEpicStatus(epic);
            save(file, dir, fileBackedTasksManager);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    public HashMap clearAllTasks(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        for (Integer idNumber : taskList.keySet()) {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
            }
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
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
            }
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
            for (Integer subtaskIdNumber : subTaskList.keySet()) {
                if (taskInProgressList.contains(subtaskIdNumber)){
                    taskInProgressList.clear();
                }
            }
            subTaskList.clear();
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
        if (taskInProgressList.contains(idNumber)){
            System.out.println("Задача в процессе выполнения. Для начала завершите ее");
        } else {
            prioritizedTasksList.remove(taskList.get(idNumber));
            newTask.setStatus(Status.NEW);
            newTask.setId(idNumber);
            taskList.put(idNumber, newTask);
            prioritizedTasksList.add(newTask);
        }
        save(file, dir, fileBackedTasksManager);
        return taskList;
    }

    public HashMap renewEpicById(Epic newEpic, int idNumber, File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        if (taskInProgressList.contains(idNumber)){
            System.out.println("Задача в процессе выполнения. Для начала завершите ее");
        } else {
            clearAllSubTasks(idNumber, file, dir, fileBackedTasksManager);
            prioritizedTasksList.remove(epicList.get(idNumber));
            newEpic.setStatus(Status.NEW);
            newEpic.setId(idNumber);
            epicList.put(idNumber, newEpic);
            prioritizedTasksList.add(epicList.get(idNumber));
        }
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
                if (taskInProgressList.contains(idNumber)){
                    System.out.println("Задача в процессе выполнения. Для начала завершите ее");
                } else {
                    if (prioritizedTasksList.contains(subTaskList.get(idNumber))){
                        prioritizedTasksList.remove(subTaskList.get(idNumber));
                    }
                    newSubTask.setStatus(Status.NEW);
                    newSubTask.setId(idNumber);
                    subTaskList.put(idNumber, newSubTask);
                    prioritizedTasksList.add(subTaskList.get(idNumber));
                }
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
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
                System.out.println("Вы удалили незаконченную задачу");
            }
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
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
                System.out.println("Вы удалили незаконченную задачу");
            }
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
            if (taskInProgressList.contains(subIdNumber)){
                taskInProgressList.clear();
                System.out.println("Вы удалили незаконченную задачу");
            }
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

}
