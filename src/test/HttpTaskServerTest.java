package test;

import com.google.gson.Gson;
import manager.HttpTaskServer;
import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    HttpTaskServer httpTaskServer;
    HttpClient client = HttpClient.newHttpClient();
    Task task = new Task();
    Epic epic = new Epic();
    Subtask subtask = new Subtask();
    String startTime = "2022-05-01T21:46:39.110446100";
    String startTime1 = "2022-07-01T21:46:39.110446100";
    int duration = 3;
    int duration1 = 3;

    public HttpResponse<String> get(HttpClient client, String urld) throws IOException, InterruptedException {
        URI url = URI.create(urld);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(HttpClient client, String urld, Task task) throws IOException, InterruptedException {
        URI url = URI.create(urld);
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(HttpClient client, String urld) throws IOException, InterruptedException {
        URI url = URI.create(urld);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/task/", task);
        assertEquals(task.toString(), response.body(), "Таска не создалась");

    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/epic/", epic);
        assertEquals(epic.toString(), response.body(), "Эпик не создался");
        System.out.println(response.body());
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/subtask/", subtask);
        assertEquals(subtask.toString(), response.body(), "Субтаска не создался");
        System.out.println(response.body());
    }

    @Test
    public void getTaskList() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/taskList/");
        assertEquals(inMemoryTaskManager.getTasksList().toString(), response.body(), "Списка тасок нет");
        assertFalse(inMemoryTaskManager.getTasksList().isEmpty(), "Списка тасок нет");
        System.out.println(response.body());
    }

    @Test
    public void getEpicList() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/epicList/");
        assertEquals(inMemoryTaskManager.getEpicsList().toString(), response.body(), "Списка эпиков нет");
        assertFalse(inMemoryTaskManager.getEpicsList().isEmpty(), "Списка эпиков нет");
        System.out.println(response.body());
    }

    @Test
    public void getSubtaskList() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/subTaskList/?" + epic.getId());
        assertEquals(inMemoryTaskManager.getEpicsList().get(1).getSubTasksList().toString(), response.body(), "Списка субтасков нет");
        assertFalse(epic.getSubTasksList().isEmpty(), "Списка субтасков нет");
        System.out.println(response.body());
    }

    @Test
    public void removeAllTasks() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/allTask/");
        assertTrue(inMemoryTaskManager.getTasksList().isEmpty(), "Таски не удалились");
        assertEquals(response.body(), "{}", "Таски не удалились");
        System.out.println(response.body());
    }

    @Test
    public void removeAllEpics() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/allEpic/");
        assertTrue(inMemoryTaskManager.getEpicsList().isEmpty(), "Эпики не удалились");
        assertEquals(response.body(), "{}", "Эпики не удалились");
        System.out.println(response.body());
    }

    @Test
    public void removeAllSubtasks() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/allSubtask/?" + epic.getId());
        assertTrue(epic.getSubTasksList().isEmpty(), "Субтаски не удалились");
        assertEquals(response.body(), "{}", "Субтаски не удалились");
        System.out.println(response.body());
    }

    @Test
    public void getTaskByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        inMemoryTaskManager.createEpic(epic, startTime1, duration1);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/getAnytask/?" + task.getId());
        HttpResponse<String> response1 = get(client, "http://localhost:8080/tasks/getAnytask/?" + epic.getId());
        assertEquals(task.toString(), response.body(), "Таску не получили");
        assertEquals(epic.toString(), response1.body(), "Эпик не получили");
        System.out.println(response.body());
        System.out.println(response1.body());
    }

    @Test
    public void getSubtaskByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/getSubtask/?" + subtask.getId() + "&" + epic.getId());
        assertEquals(subtask.toString(), response.body(), "Субтаску не получили");
        System.out.println(response.body());
    }

    @Test
    public void renewTask() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(task), "Таску не создали");
        Task newTask = new Task();
        inMemoryTaskManager.renewTaskById(newTask, task.getId());
        assertTrue(inMemoryTaskManager.getTasksList().containsValue(newTask), "Таску не создали");
        assertFalse(inMemoryTaskManager.getTasksList().containsValue(task), "Таску не создали");
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/newTask/", newTask);
        assertEquals(newTask.toString(), response.body(), "Таску не обновили");
        HttpResponse<String> response1 = get(client, "http://localhost:8080/tasks/taskList/");
        assertFalse(inMemoryTaskManager.getTasksList().containsValue(task), "Старая таска есть в списке");
        assertEquals(inMemoryTaskManager.getTasksList().toString(), response1.body(), "Таску не обновили");
        System.out.println(response1.body());
    }

    @Test
    public void renewEpic() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(epic), "Эпик не создали");
        Epic newEpic = new Epic();
        inMemoryTaskManager.renewEpicById(newEpic, epic.getId());
        assertTrue(inMemoryTaskManager.getEpicsList().containsValue(newEpic), "Эпик не создали");
        assertFalse(inMemoryTaskManager.getEpicsList().containsValue(epic), "Эпик не создали");
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/newEpic/", newEpic);
        assertEquals(newEpic.toString(), response.body(), "Эпик не обновили");
        HttpResponse<String> response1 = get(client, "http://localhost:8080/tasks/epicList/");
        assertFalse(inMemoryTaskManager.getEpicsList().containsValue(epic), "Старый эпик есть в списке");
        assertEquals(inMemoryTaskManager.getEpicsList().toString(), response1.body(), "Эпик не обновили");
        System.out.println(response1.body());
    }

    @Test
    public void renewSubtask() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        assertTrue(epic.getSubTasksList().containsValue(subtask), "Субтаску не создали");
        Subtask newSubtask = new Subtask();
        inMemoryTaskManager.renewSubTaskById(epic, newSubtask, subtask.getId());
        assertTrue(epic.getSubTasksList().containsValue(newSubtask), "Субтаску не создали");
        assertFalse(epic.getSubTasksList().containsValue(subtask), "Субтаску не создали");
        HttpResponse<String> response = post(client, "http://localhost:8080/tasks/newSubtask/", newSubtask);
        assertEquals(newSubtask.toString(), response.body(), "Субтаску не обновили");
        HttpResponse<String> response1 = get(client, "http://localhost:8080/tasks/subTaskList/?" + epic.getId());
        assertFalse(epic.getSubTasksList().containsValue(subtask), "Старая субтаска есть в списке");
        assertEquals(epic.getSubTasksList().toString(), response1.body(), "Субтаску не обновили");
        System.out.println(response1.body());
    }

    @Test
    public void removeTaskByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/task/?" + task.getId());
        assertEquals("{}", response.body(), "Таска не удалилась");
        System.out.println(response.body());
    }

    @Test
    public void removeEpicByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/epic/?" + epic.getId());
        assertEquals("{}", response.body(), "Эпик не удалился");
        System.out.println(response.body());
    }

    @Test
    public void removeSubtaskByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration1);
        HttpResponse<String> response = delete(client, "http://localhost:8080/tasks/subtask/?" + subtask.getId() + "&" + epic.getId());
        assertEquals("{}", response.body(), "Субтаска не удалилась");
        System.out.println(response.body());
    }

    @Test
    public void getTaskStatusByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/taskStatus/?" + task.getId());
        assertEquals("NEW", response.body(), "Статус не верен");
        System.out.println(response.body());
    }

    @Test
    public void getEpicStatusByID() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/epicStatus/?" + epic.getId());
        assertEquals("NEW", response.body(), "Статус не верен");
        System.out.println(response.body());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.getAnyTaskById(1);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/history/");
        assertEquals(inMemoryTaskManager.getInMemoryHistoryManager().getHistory().toString(), response.body(), "Истории просмотров нет");
        System.out.println(response.body());
    }

    @Test
    public void getPrioritizedTasksList() throws IOException, InterruptedException {
        inMemoryTaskManager.createTask(task, startTime, duration);
        Task task1 = new Task();
        String startTime1 = "2022-06-01T21:46:39.110446100";
        int duration1 = 3;
        inMemoryTaskManager.createTask(task1, startTime1, duration1);
        Task task2 = new Task();
        String startTime2 = "2022-07-01T21:46:39.110446100";
        int duration2 = 3;
        inMemoryTaskManager.createTask(task2, startTime2, duration2);
        HttpResponse<String> response = get(client, "http://localhost:8080/tasks/");
        assertEquals(inMemoryTaskManager.getPrioritizedTasksList().toString(), response.body(), "Списка задач по времени нет");
        System.out.println(response.body());
    }
}
