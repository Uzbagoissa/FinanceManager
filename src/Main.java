import manager.HTTPTaskManager;
import manager.KVServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.Scanner;

public class Main implements Serializable {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");

            System.out.println("1 - Посмотреть список обычных задач");
            System.out.println("2 - Посмотреть список эпик задач");
            System.out.println("3 - Посмотреть список подзадач нужного эпика");

            System.out.println("4 - Создать новую обычную задачу");
            System.out.println("5 - Создать новую эпик задачу");
            System.out.println("6 - Создать подзадачу требуемого эпик");

            System.out.println("7 - Удалить все обычные задачи");
            System.out.println("8 - Удалить все эпик задачи");
            System.out.println("80 - Удалить все подзадачи требуемого эпик");

            System.out.println("9 - Найти любую задачу по ID");
            System.out.println("90 - Найти подзадачу требуемого эпик");

            System.out.println("10 - Обновить обычную задачу");
            System.out.println("11 - Обновить эпик задачу");
            System.out.println("12 - Обновить подзадачу требуемого эпик");

            System.out.println("13 - Удалить обычную задачу по ID");
            System.out.println("14 - Удалить эпик задачу по ID");
            System.out.println("15 - Удалить подзадачу требуемого эпик");

            System.out.println("16 - Посмотреть список всех задач");

            System.out.println("17 - Узнать статус задачи по ID");
            System.out.println("18 - Узнать статус эпик задачи по ID");
            System.out.println("19 - Показать последние просмотренные задачи");

            System.out.println("20 - Показать отсортированный список задач");

            System.out.println("0 - Выход");

            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println(httpTaskManager.load("taskList"));

            } else if (command == 2) {
                System.out.println(httpTaskManager.load("epicList"));

            } else if (command == 3) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                System.out.println(((Epic) httpTaskManager.load("epicList").get(idNumber)).getSubTasksList());

            } else if (command == 4) {
                Task task = new Task();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                System.out.println(httpTaskManager.createTask(task, startTime, duration));

            } else if (command == 5) {
                Epic epic = new Epic();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                System.out.println(httpTaskManager.createEpic(epic, startTime, duration));

            } else if (command == 6) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) httpTaskManager.getAnyTaskById(idNumber);
                Subtask subtask = new Subtask();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                System.out.println(httpTaskManager.createSubTask(epic, subtask, startTime, duration));

            } else if (command == 7) {
                System.out.println(httpTaskManager.clearAllTasks());

            } else if (command == 8) {
                System.out.println(httpTaskManager.clearAllEpic());

            } else if (command == 80) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.clearAllSubTasks(idNumber));

            } else if (command == 9) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.getAnyTaskById(idNumber));

            } else if (command == 90) {
                System.out.println("Введите ID эпик");
                int epicIdNumber = scanner.nextInt();
                Epic epic = (Epic) httpTaskManager.getAnyTaskById(epicIdNumber);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subtaskIdNumber = scanner.nextInt();
                    System.out.println(httpTaskManager.getSubTaskById(epicIdNumber, subtaskIdNumber));
                }

            } else if (command == 10) {
                System.out.println("Введите ID");
                Task newTask = new Task();
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.renewTaskById(newTask, idNumber));

            } else if (command == 11) {
                System.out.println("Введите ID эпик");
                Epic newEpic = new Epic();
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.renewEpicById(newEpic, idNumber));

            } else if (command == 12) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) httpTaskManager.getAnyTaskById(idNumber);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    Subtask newSubTask = new Subtask();
                    int subIdNumber = scanner.nextInt();
                    System.out.println(httpTaskManager.renewSubTaskById(epic, newSubTask, subIdNumber));
                }

            } else if (command == 13) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.clearTaskById(idNumber));

            } else if (command == 14) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.clearEpicById(idNumber));

            } else if (command == 15) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) httpTaskManager.getAnyTaskById(idNumber);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subIdNumber = scanner.nextInt();
                    System.out.println(httpTaskManager.clearSubTaskById(epic, subIdNumber));
                }

            } else if (command == 16) {
                System.out.println(httpTaskManager.getTasksList() + " " + httpTaskManager.getEpicsList());

            } else if (command == 17) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.getTaskStatusById(idNumber));

            } else if (command == 18) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(httpTaskManager.getEpicStatusById(idNumber));

            } else if (command == 19) {
                System.out.println(httpTaskManager.load("history"));

            } else if (command == 20) {
                System.out.println(httpTaskManager.load("prioritizedTasksList"));

            } else if (command == 0) {
                kvServer.stop();
                break;
            }
        }
    }
}
