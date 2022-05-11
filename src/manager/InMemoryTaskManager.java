package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager, Serializable {
    private final HashMap<Integer, Task> taskList;
    private final HashMap<Integer, Epic> epicList;
    private final ArrayList<Task> prioritizedTasksList;
    private int idNumber;
    private final HistoryManager inMemoryHistoryManager;
    TaskComparator taskComparator = new TaskComparator();

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.prioritizedTasksList = new ArrayList<>();
        this.idNumber = 0;
        this.inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getPrioritizedTasksList() {
        prioritizedTasksList.sort(taskComparator);
        return prioritizedTasksList;
    }

    @Override
    public HashMap<Integer, Task> getTasksList() {
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> getEpicsList() {
        return epicList;
    }

    @Override
    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public HashMap<Integer, Task> createTask(Task task, String startTime, int duration) {
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> createEpic(Epic epic, String startTime, int duration) {
        LocalDateTime epicStartTime = LocalDateTime.parse(startTime);
        for (Task taskk : prioritizedTasksList) {
            if ((epicStartTime.isAfter(taskk.getStartTime()) && epicStartTime.isBefore(taskk.getEndTime())) || epicStartTime.isEqual(taskk.getStartTime()) || epicStartTime.isEqual(taskk.getEndTime())){
                System.out.println("Время занято выполнением другой задачи. Выберите другое время. " + "Позже, чем " + taskk.getEndTime() + "\n" + " или раньше, чем " + taskk.getStartTime());
                return epicList;
            }
        }
        LocalDateTime epicFinishTime = epicStartTime.plusHours(duration);
        if ((epicFinishTime.isBefore(epicStartTime)) || epicFinishTime.isEqual(epicStartTime)){
            System.out.println("Время окончания задачи должно быть позже времени начала задачи");
            return epicList;
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
        if ((epicFinishTime.isAfter(minStartTaskTime)) || epicFinishTime.isEqual(minStartTaskTime)){
            System.out.println("Выберите более раннее время окончания задачи, чем " + minStartTaskTime);
            return epicList;
        }
        idNumber += 1;
        epic.setId(idNumber);
        epicList.put(idNumber, epic);
        epicList.get(idNumber).setStartTime(epicStartTime);
        Duration epicDuration = Duration.ofHours(duration);
        epicList.get(idNumber).setDuration(epicDuration);
        epicList.get(idNumber).setEndTime(epicFinishTime);
        prioritizedTasksList.add(epic);
        epic.setStatus(Status.NEW);
        return epicList;
    }

    @Override
    public HashMap<Integer, Subtask> createSubTask(Epic epic, Subtask subtask, String startTime, int duration) {
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
        } catch (Throwable ex){
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    @Override
    public HashMap<Integer, Task> clearAllTasks() {
        for (Integer idNumber : taskList.keySet()) {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
        }
        taskList.clear();
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> clearAllEpic() {
        for (Integer idNumber : epicList.keySet()) {
            inMemoryHistoryManager.remove(epicList.get(idNumber));
            HashMap<Integer, Subtask> subTaskList = epicList.get(idNumber).getSubTasksList();
            for (Subtask subtask : subTaskList.values()) {
                prioritizedTasksList.remove(subtask);
            }
            prioritizedTasksList.remove(epicList.get(idNumber));
        }
        epicList.clear();
        return epicList;
    }

    @Override
    public HashMap<Integer, Subtask> clearAllSubTasks(int idNumber) {
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
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    @Override
    public Task getAnyTaskById(int idNumber) {
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
            System.out.println("Введите правильное значение");
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
            System.out.println("Введите правильное значение");
        }
        return subtask;
    }

    @Override
    public HashMap<Integer, Task> renewTaskById(Task newTask, int idNumber) {
        prioritizedTasksList.remove(taskList.get(idNumber));
        newTask.setStatus(Status.NEW);
        newTask.setId(idNumber);
        taskList.put(idNumber, newTask);
        prioritizedTasksList.add(newTask);
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> renewEpicById(Epic newEpic, int idNumber) {
        clearAllSubTasks(idNumber);
        prioritizedTasksList.remove(epicList.get(idNumber));
        newEpic.setStatus(Status.NEW);
        newEpic.setId(idNumber);
        epicList.put(idNumber, newEpic);
        prioritizedTasksList.add(epicList.get(idNumber));
        return epicList;
    }

    @Override
    public HashMap<Integer, Subtask> renewSubTaskById(Epic epic, Subtask newSubTask, int idNumber) {
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
            }
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return subTaskList;
    }

    @Override
    public HashMap<Integer, Task> clearTaskById(int idNumber) {
        try {
            inMemoryHistoryManager.remove(taskList.get(idNumber));
            prioritizedTasksList.remove(taskList.get(idNumber));
            taskList.remove(idNumber);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return taskList;
    }

    @Override
    public HashMap<Integer, Epic> clearEpicById(int idNumber) {
        try {
            inMemoryHistoryManager.remove(epicList.get(idNumber));
            prioritizedTasksList.remove(epicList.get(idNumber));
            clearAllSubTasks(idNumber);
            epicList.remove(idNumber);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
        }
        return epicList;
    }

    @Override
    public HashMap<Integer, Subtask> clearSubTaskById(Epic epic, int subIdNumber) {
        HashMap<Integer, Subtask> subTaskList = null;
        try {
            subTaskList = epic.getSubTasksList();
            inMemoryHistoryManager.remove(subTaskList.get(subIdNumber));
            prioritizedTasksList.remove(subTaskList.get(subIdNumber));
            subTaskList.remove(subIdNumber);
            changeEpicStatus(epic);
        } catch (Throwable ex) {
            System.out.println("Введите правильное значение");
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
            System.out.println("Введите правильное значение");
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
            System.out.println("Введите правильное значение");
        }
        return o;
    }

    @Override
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

    @Override
    public void changeEpicTime(Epic epic, Subtask subtask) {
        if (epic.getSubTasksList().size() == 1){
            LocalDateTime epicStartTime = subtask.getStartTime();
            epic.setStartTime(epicStartTime);
            LocalDateTime epicEndTime = subtask.getEndTime();
            epic.setEndTime(epicEndTime);
            Duration epicDuration = subtask.getDuration();
            epic.setDuration(epicDuration);
        } else if (epic.getSubTasksList().size() > 1){
            LocalDateTime epicEndTime = subtask.getEndTime();
            epic.setEndTime(epicEndTime);
            Duration epicDuration = Duration.between(epic.getStartTime(), subtask.getEndTime());
            epic.setDuration(epicDuration);
        }
    }

}