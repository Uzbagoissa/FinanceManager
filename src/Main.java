import manager.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.Scanner;

public class Main implements Serializable {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String dir = System.getProperty("user.dir");
        File file = new File("backend.txt");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file, dir);
        fileBackedTasksManager = fileBackedTasksManager.loadFromFile(file, dir);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");

            System.out.println("1 - Посмотреть список обычных задач");
            System.out.println("2 - Посмотреть список эпик задач");
            System.out.println("3 - Посмотреть список подзадач требуемого эпик");

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
                System.out.println(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager));

            } else if (command == 2) {
                System.out.println(fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager));

            } else if (command == 3) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(idNumber, file, dir, fileBackedTasksManager);
                System.out.println(epic.getSubTasksList());

            } else if (command == 4) {
                Task task = new Task();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                System.out.println(fileBackedTasksManager.createTask(task, startTime, duration, file, dir, fileBackedTasksManager));

            } else if (command == 5) {
                Epic epic = new Epic();
                System.out.println(fileBackedTasksManager.createEpic(epic, file, dir, fileBackedTasksManager));

            } else if (command == 6) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(idNumber, file, dir, fileBackedTasksManager);
                Subtask subtask = new Subtask();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                System.out.println(fileBackedTasksManager.createSubTask(epic, subtask, startTime, duration, file, dir, fileBackedTasksManager));

            } else if (command == 7) {
                System.out.println(fileBackedTasksManager.clearAllTasks(file, dir, fileBackedTasksManager));

            } else if (command == 8) {
                System.out.println(fileBackedTasksManager.clearAllEpic(file, dir, fileBackedTasksManager));

            } else if (command == 80) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.clearAllSubTasks(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 9) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getAnyTaskById(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 90) {
                System.out.println("Введите ID эпик");
                int epicIdNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(epicIdNumber, file, dir, fileBackedTasksManager);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subtaskIdNumber = scanner.nextInt();
                    System.out.println(fileBackedTasksManager.getSubTaskById(epicIdNumber, subtaskIdNumber, file, dir, fileBackedTasksManager));
                }

            } else if (command == 10) {
                System.out.println("Введите ID");
                Task newTask = new Task();
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.renewTaskById(newTask, idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 11) {
                System.out.println("Введите ID эпик");
                Epic newEpic = new Epic();
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.renewEpicById(newEpic, idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 12) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(idNumber, file, dir, fileBackedTasksManager);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    Subtask newSubTask = new Subtask();
                    int subIdNumber = scanner.nextInt();
                    System.out.println(fileBackedTasksManager.renewSubTaskById(epic, newSubTask, subIdNumber, file, dir, fileBackedTasksManager));
                }

            } else if (command == 13) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.clearTaskById(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 14) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.clearEpicById(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 15) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(idNumber, file, dir, fileBackedTasksManager);
                if (epic.getSubTasksList().isEmpty()){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subIdNumber = scanner.nextInt();
                    System.out.println(fileBackedTasksManager.clearSubTaskById(epic, subIdNumber, file, dir, fileBackedTasksManager));
                }

            } else if (command == 16) {
                System.out.println(fileBackedTasksManager.getTasksList(file, dir, fileBackedTasksManager) + " " + fileBackedTasksManager.getEpicsList(file, dir, fileBackedTasksManager));

            } else if (command == 17) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getTaskStatusById(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 18) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getEpicStatusById(idNumber, file, dir, fileBackedTasksManager));

            } else if (command == 19) {
                System.out.println(fileBackedTasksManager.getInMemoryHistoryManager(file, dir, fileBackedTasksManager).getHistory());

            } else if (command == 20) {
                System.out.println(fileBackedTasksManager.getPrioritizedTasksList(file, dir, fileBackedTasksManager));

            } else if (command == 0) {
                break;
            }
        }
    }
}
