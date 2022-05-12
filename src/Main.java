import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.HttpTaskServer;
import manager.KVServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main implements Serializable {
    private static final int PORT = 8080;
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String dir = System.getProperty("user.dir");
        File file = new File("backend.txt");

        FileBackedTasksManager fileBackedTasksManager;
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file, dir);

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServer(fileBackedTasksManager));
        httpServer.start();

        new KVServer().start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

        HttpClient client = HttpClient.newHttpClient();

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
                URI url = URI.create("http://localhost:8080/tasks/taskList/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 2) {
                URI url = URI.create("http://localhost:8080/tasks/epicList/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 3) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/subTaskList/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 4) {
                Task task = new Task();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                fileBackedTasksManager.createTask(task, startTime, duration, fileBackedTasksManager);
                URI url = URI.create("http://localhost:8080/tasks/task/");
                Gson gson = new Gson();
                String json = gson.toJson(task);
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 5) {
                Epic epic = new Epic();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                fileBackedTasksManager.createEpic(epic, startTime, duration, fileBackedTasksManager);
                URI url = URI.create("http://localhost:8080/tasks/epic/");
                Gson gson = new Gson();
                String json = gson.toJson(epic);
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 6) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                Epic epic = (Epic) fileBackedTasksManager.getAnyTaskById(idNumber, fileBackedTasksManager);
                Subtask subtask = new Subtask();
                System.out.println("Введите дату и время начала задачи по образцу: 2022-05-01T21:46:39.110446100");
                String startTime = scanner.next();
                System.out.println("Введите продолжительность задачи в часах");
                int duration = scanner.nextInt();
                fileBackedTasksManager.createSubTask(epic, subtask, startTime, duration, fileBackedTasksManager);
                URI url = URI.create("http://localhost:8080/tasks/subtask/");
                Gson gson = new Gson();
                String json = gson.toJson(subtask);
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 7) {
                URI url = URI.create("http://localhost:8080/tasks/allTask/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 8) {
                URI url = URI.create("http://localhost:8080/tasks/allEpic/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 80) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/allSubtask/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 9) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/getAnytask/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 90) {
                System.out.println("Введите ID эпик");
                int epicIdNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/subTaskList/?" + epicIdNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.body().contains("{}")){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subtaskIdNumber = scanner.nextInt();
                    URI url1 = URI.create("http://localhost:8080/tasks/getSubtask/?" + subtaskIdNumber + "&" + epicIdNumber);
                    HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
                    HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response1.body());
                }

            } else if (command == 10) {
                System.out.println("Введите ID");
                Task newTask = new Task();
                int idNumber = scanner.nextInt();
                fileBackedTasksManager.renewTaskById(newTask, idNumber, fileBackedTasksManager);
                URI url = URI.create("http://localhost:8080/tasks/newTask/");
                Gson gson = new Gson();
                String json = gson.toJson(newTask);
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 11) {
                System.out.println("Введите ID эпик");
                Epic newEpic = new Epic();
                int idNumber = scanner.nextInt();
                fileBackedTasksManager.renewEpicById(newEpic, idNumber, fileBackedTasksManager);
                URI url = URI.create("http://localhost:8080/tasks/newEpic/");
                Gson gson = new Gson();
                String json = gson.toJson(newEpic);
                final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 12) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/subTaskList/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.body().contains("{}")){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    Subtask newSubTask = new Subtask();
                    int subIdNumber = scanner.nextInt();
                    fileBackedTasksManager.renewSubTaskById(fileBackedTasksManager.getEpicsList().get(idNumber), newSubTask, subIdNumber, fileBackedTasksManager);
                    URI url1 = URI.create("http://localhost:8080/tasks/newSubtask/");
                    Gson gson = new Gson();
                    String json = gson.toJson(newSubTask);
                    final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                    HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
                    HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response1.body());
                }

            } else if (command == 13) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/task/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 14) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/epic/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 15) {
                System.out.println("Введите ID эпик");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/subTaskList/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.body().contains("{}")){
                    System.out.println("В этом эпике нет подзадач");
                } else {
                    System.out.println("Введите ID подзадачи");
                    int subIdNumber = scanner.nextInt();
                    URI url1 = URI.create("http://localhost:8080/tasks/subtask/?" + subIdNumber + "&" + idNumber);
                    HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
                    HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response1.body());
                }

            } else if (command == 16) {
                URI url = URI.create("http://localhost:8080/tasks/alltasks/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 17) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/taskStatus/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 18) {
                System.out.println("Введите ID");
                int idNumber = scanner.nextInt();
                URI url = URI.create("http://localhost:8080/tasks/epicStatus/?" + idNumber);
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 19) {
                URI url = URI.create("http://localhost:8080/tasks/history/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 20) {
                URI url = URI.create("http://localhost:8080/tasks/");
                HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } else if (command == 0) {
                httpServer.stop(1);
                break;

            }
        }
    }
}
