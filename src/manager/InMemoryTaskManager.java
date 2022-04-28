package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager, Serializable {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private ArrayList<Integer> taskInProgressList;
    private ArrayList<Task> prioritizedTasksList;
    private int idNumber;
    private HistoryManager inMemoryHistoryManager;
    TaskComparator taskComparator = new TaskComparator();

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.taskInProgressList = new ArrayList<>();
        this.prioritizedTasksList = new ArrayList<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    public void startTask(int idNumber, String startTime){
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void startEpic(int idNumber, int subIdNumber, String startTime) {
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void finishTask(int idNumber, String finishTime) {
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
    }

    public void finishEpic(int idNumber, int subIdNumber, String finishTime) {
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
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

    public ArrayList getPrioritizedTasksList() {
        prioritizedTasksList.sort(taskComparator);
        return prioritizedTasksList;
    }

    public HashMap getTasksList() {
        return taskList;
    }

    public HashMap getEpicsList() {
        return epicList;
    }

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public HashMap createTask(Task task) {
        idNumber += 1;
        task.setId(idNumber);
        taskList.put(idNumber, task);
        prioritizedTasksList.add(task);
        task.setStatus(Status.NEW);
        return taskList;
    }

    @Override
    public HashMap createEpic(Epic epic) {
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        prioritizedTasksList.add(epic);
        epic.setStatus(Status.NEW);
        return epicList;
    }

    @Override
    public HashMap createSubTask(Epic epic, Subtask subtask) {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            idNumber += 1;
            subTaskList = epic.getSubTasksList();
            subtask.setId(idNumber);
            subTaskList.put(idNumber, subtask);
            prioritizedTasksList.add(subtask);
            subtask.setStatus(Status.NEW);
            changeEpicStatus(epic);
        } catch (Throwable ex) {
            System.out.println("Такой Epic задачи в списке нет");
        }
        return subTaskList;
    }

    @Override
    public HashMap clearAllTasks() {
        for (Integer idNumber : taskList.keySet()) {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
            }
        }
        taskList.clear();
        return taskList;
    }

    @Override
    public HashMap clearAllEpic(){
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
        return epicList;
    }

    @Override
    public HashMap clearAllSubTasks(int idNumber){
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
        } catch (Throwable ex) {
            System.out.println("Такой Epic задачи в списке нет");
        }
        return subTaskList;
    }

    @Override
    public Task getAnyTaskById(int idNumber){
        Task task = null;
        try {
            if (taskList.get(idNumber) != null) {
                task = taskList.get(idNumber);
                inMemoryHistoryManager.add(task);
            } else if (epicList.get(idNumber) != null) {
                task = epicList.get(idNumber);
                inMemoryHistoryManager.add(task);
            }
        } catch (Throwable ex){
            System.out.println("Такой задачи в списке нет");
        }
        return task;
    }

    @Override
    public Subtask getSubTaskById(int epicIdNumber, int subtaskIdNumber){
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
        } catch (Throwable ex){
            System.out.println("Такой Epic задачи в списке нет");
        }
        return subtask;
    }

    @Override
    public HashMap renewTaskById(Task newTask, int idNumber) {
        if (taskInProgressList.contains(idNumber)){
            System.out.println("Задача в процессе выполнения. Для начала завершите ее");
        } else {
            prioritizedTasksList.remove(taskList.get(idNumber));
            newTask.setStatus(Status.NEW);
            newTask.setId(idNumber);
            taskList.put(idNumber, newTask);
            prioritizedTasksList.add(newTask);
        }
        return taskList;
    }

    @Override
    public HashMap renewEpicById(Epic newEpic, int idNumber) {
        if (taskInProgressList.contains(idNumber)){
            System.out.println("Задача в процессе выполнения. Для начала завершите ее");
        } else {
            clearAllSubTasks(idNumber);
            prioritizedTasksList.remove(epicList.get(idNumber));
            newEpic.setStatus(Status.NEW);
            newEpic.setId(idNumber);
            epicList.put(idNumber, newEpic);
            prioritizedTasksList.add(epicList.get(idNumber));
        }
        return epicList;
    }

    @Override
    public HashMap renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
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
            }
        } catch (Throwable ex) {
            System.out.println("Такой Epic задачи в списке нет");
        }
        return subTaskList;
    }

    @Override
    public HashMap clearTaskById(int idNumber) {
        try {
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
                System.out.println("Вы удалили незаконченную задачу");
            }
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
            taskList.remove(idNumber);
        } catch (Throwable ex) {
            System.out.println("Такой Task задачи в списке нет");
        }
        return taskList;
    }

    @Override
    public HashMap clearEpicById(int idNumber) {
        try {
            if (taskInProgressList.contains(idNumber)){
                taskInProgressList.clear();
                System.out.println("Вы удалили незаконченную задачу");
            }
            inMemoryHistoryManager.remove(epicList.get(idNumber));
            prioritizedTasksList.remove(epicList.get(idNumber));
            clearAllSubTasks(idNumber);
            epicList.remove(idNumber);
        } catch (Throwable ex) {
            System.out.println("Такой Epic задачи в списке нет");
        }
        return epicList;
    }

    @Override
    public HashMap clearSubTaskById(Epic epic, int subIdNumber) {
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
        } catch (Throwable ex) {
            System.out.println("Такой Subtask задачи в списке нет");
        }
        return subTaskList;
    }

    @Override
    public String getTaskStatusById(int idNumber) {
        String o = "";
        try {
            Task task = taskList.get(idNumber);
            o = String.valueOf(task.getStatus());
        } catch (Throwable ex){
            System.out.println("Такой Task задачи в списке нет");
        }
        return o;
    }

    @Override
    public String getEpicStatusById(int idNumber) {
        String o = "";
        try {
            Epic epic = epicList.get(idNumber);
            o = String.valueOf(epic.getStatus());
        } catch (Throwable ex){
            System.out.println("Такой Epic задачи в списке нет");
        }
        return o;
    }

}