import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        while (true) {/*Для проверки работы методов создал консольное меню.
        Для удобства создание и удаление объектов выводится в печать*/
            System.out.println("Выберите действие:");

            System.out.println("1 - Посмотреть список обычных задач");
            System.out.println("2 - Посмотреть список model.Epic задач");
            System.out.println("3 - Посмотреть список подзадач требуемого model.Epic");

            System.out.println("4 - Создать новую обычную задачу");
            System.out.println("5 - Создать новую model.Epic задачу");
            System.out.println("6 - Создать подзадачу требуемого model.Epic");

            System.out.println("7 - Удалить все обычные задачи");
            System.out.println("8 - Удалить все model.Epic задачи");

            System.out.println("9 - Найти любую задачу по ID");

            System.out.println("10 - Обновить обычную задачу");
            System.out.println("11 - Обновить model.Epic задачу");
            System.out.println("12 - Обновить подзадачу требуемого model.Epic");

            System.out.println("13 - Удалить обычную задачу по ID");
            System.out.println("14 - Удалить model.Epic задачу по ID");
            System.out.println("15 - Удалить подзадачу требуемого model.Epic");

            System.out.println("16 - Посмотреть список всех задач");

            System.out.println("17 - Узнать статус задачи по ID");
            System.out.println("18 - Узнать статус model.Epic задачи по ID");
            System.out.println("0 - Выход");

            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println(manager.getTasksList());

            } else if (command == 2) {
                System.out.println(manager.getEpicsList());

            } else if (command == 3) {
                System.out.println("Введите ID model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) manager.getAnyTaskById(idNumber);
                System.out.println(epic.getSubTasksList());

            } else if (command == 4) {
                Task task = new Task();
                System.out.println(manager.createTask(task));

            } else if (command == 5) {
                Epic epic = new Epic();
                System.out.println(manager.createEpic(epic));
            } else if (command == 6) {
                System.out.println("Введите ID model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) manager.getAnyTaskById(idNumber);
                Subtask subtask = new Subtask();
                System.out.println(epic.createSubTask(epic, subtask));

            } else if (command == 7) {
                System.out.println(manager.clearAllTasks());

            } else if (command == 8) {
                System.out.println(manager.clearAllEpic());

            } else if (command == 9) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(manager.getAnyTaskById(idNumber));

            } else if (command == 10) {
                System.out.println("Введите ID");
                Task newTask = new Task();
                int idNumber = scanner.nextInt();
                System.out.println("Установить статус");// насколько я понял значение статуса приходит извне, по-этому для удобства проверки метода вводим статус из консоли
                String status = scanner.next();
                System.out.println(manager.renewTaskById(newTask, idNumber, status));

            } else if (command == 11) {
                System.out.println("Введите ID model.Epic");
                Epic newEpic = new Epic();
                int idNumber = scanner.nextInt();
                System.out.println("Установить статус");//вводим статус из консоли
                String status = scanner.next();
                System.out.println(manager.renewEpicById(newEpic, idNumber, status));

            } else if (command == 12) {
                System.out.println("Введите ID model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) manager.getAnyTaskById(idNumber);
                System.out.println("Введите ID подзадачи");
                Subtask newSubTask = new Subtask();
                int subIdNumber = scanner.nextInt();
                System.out.println("Установить статус");//вводим статус из консоли
                String status = scanner.next();
                System.out.println(epic.renewSubTaskById(epic, newSubTask, subIdNumber, status));

            } else if (command == 13) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(manager.clearTaskById(idNumber));

            } else if (command == 14) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(manager.clearEpicById(idNumber));

            } else if (command == 15) {
                System.out.println("Введите ID model.Epic");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) manager.getAnyTaskById(idNumber);
                System.out.println("Введите ID подзадачи");
                int subIdNumber = scanner.nextInt();
                System.out.println(epic.clearSubTaskById(epic, subIdNumber));

            } else if (command == 16) {
                System.out.println(manager.getTasksList() + " " + manager.getEpicsList());

            } else if (command == 17) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(manager.getTaskStatusById(idNumber));

            } else if (command == 18) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                System.out.println(manager.getEpicStatusById(idNumber));

            } else if (command == 0) {
                break;
            }
        }
    }
}
