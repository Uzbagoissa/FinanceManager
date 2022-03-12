import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        while (true) {
            System.out.println("Выберите действие:");

            System.out.println("1 - Посмотреть список обычных задач");
            System.out.println("2 - Посмотреть список model.Epic задач");
            System.out.println("3 - Посмотреть список подзадач требуемого model.Epic");

            System.out.println("4 - Создать новую обычную задачу");
            System.out.println("5 - Создать новую model.Epic задачу");
            System.out.println("6 - Создать подзадачу требуемого model.Epic");

            System.out.println("7 - Удалить все обычные задачи");
            System.out.println("8 - Удалить все model.Epic задачи");
            System.out.println("80 - Удалить все подзадачи требуемого model.Epic");

            System.out.println("9 - Найти любую задачу по ID");
            System.out.println("90 - Найти подзадачу требуемого model.Epic");

            System.out.println("10 - Обновить обычную задачу");
            System.out.println("11 - Обновить model.Epic задачу");
            System.out.println("12 - Обновить подзадачу требуемого model.Epic");

            System.out.println("13 - Удалить обычную задачу по ID");
            System.out.println("14 - Удалить model.Epic задачу по ID");
            System.out.println("15 - Удалить подзадачу требуемого model.Epic");

            System.out.println("16 - Посмотреть список всех задач");

            System.out.println("17 - Узнать статус задачи по ID");
            System.out.println("18 - Узнать статус model.Epic задачи по ID");
            System.out.println("19 - Показать последние просмотренные задачи");
            System.out.println("0 - Выход");

            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println(inMemoryTaskManager.getTasksList());

            } else if (command == 2) {
                System.out.println(inMemoryTaskManager.getEpicsList());

            } else if (command == 3) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                System.out.println(epic.getSubTasksList());

            } else if (command == 4) {
                Task task = new Task();
                System.out.println(inMemoryTaskManager.createTask(task));

            } else if (command == 5) {
                Epic epic = new Epic();
                System.out.println(inMemoryTaskManager.createEpic(epic));

            } else if (command == 6) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                Subtask subtask = new Subtask();
                System.out.println(epic.createSubTask(epic, subtask));

            } else if (command == 7) {
                System.out.println(inMemoryTaskManager.clearAllTasks());

            } else if (command == 8) {
                System.out.println(inMemoryTaskManager.clearAllEpic());

            } else if (command == 80) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                System.out.println(epic.clearAllSubTasks());

            } else if (command == 9) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getAnyTaskById(idNumber));

            } else if (command == 90) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                System.out.println("Введите ID подзадачи");
                int subIdNumber = scanner.nextInt();
                System.out.println(epic.getSubTaskById(subIdNumber));

            } else if (command == 10) {
                System.out.println("Введите ID");
                Task newTask = new Task();
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.renewTaskById(newTask, idNumber));

            } else if (command == 11) {
                System.out.println("Введите ID model.model.Epic");
                Epic newEpic = new Epic();
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.renewEpicById(newEpic, idNumber));

            } else if (command == 12) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                System.out.println("Введите ID подзадачи");
                Subtask newSubTask = new Subtask();
                int subIdNumber = scanner.nextInt();
                System.out.println(epic.renewSubTaskById(epic, newSubTask, subIdNumber));

            } else if (command == 13) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.clearTaskById(idNumber));

            } else if (command == 14) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.clearEpicById(idNumber));

            } else if (command == 15) {
                System.out.println("Введите ID model.model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) inMemoryTaskManager.getAnyTaskById(idNumber);
                System.out.println("Введите ID подзадачи");
                int subIdNumber = scanner.nextInt();
                System.out.println(epic.clearSubTaskById(epic, subIdNumber));

            } else if (command == 16) {
                System.out.println(inMemoryTaskManager.getTasksList() + " " + inMemoryTaskManager.getEpicsList());

            } else if (command == 17) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getTaskStatusById(idNumber));

            } else if (command == 18) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getEpicStatusById(idNumber));

            } else  if (command == 19){
                System.out.println(inMemoryHistoryManager.getHistory());

            } else if (command == 0) {
                break;
            }
        }
    }
}
