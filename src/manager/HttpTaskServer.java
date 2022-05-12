package manager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer implements Serializable, HttpHandler {
    FileBackedTasksManager fileBackedTasksManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();

    public HttpTaskServer(FileBackedTasksManager fileBackedTasksManager) {
        this.fileBackedTasksManager = fileBackedTasksManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        httpExchange.sendResponseHeaders(200, 0);
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();


        switch (method) {
            case "GET":
                if (path.equals("/tasks/")){
                    response = fileBackedTasksManager.getPrioritizedTasksList().toString();
                } else {
                    String[] splitStringsGet = path.split("/");
                    if (splitStringsGet[2].equals("taskList")) {
                        response = fileBackedTasksManager.getTasksList().toString();
                    } else if (splitStringsGet[2].equals("epicList")) {
                        response = fileBackedTasksManager.getEpicsList().toString();
                    } else if (splitStringsGet[2].equals("subTaskList")) {
                        int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                        response = fileBackedTasksManager.getEpicsList().get(query).getSubTasksList().toString();
                    } else if (splitStringsGet[2].equals("getAnytask")) {
                        int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                        response = fileBackedTasksManager.getAnyTaskById(query, fileBackedTasksManager).toString();
                    } else if (splitStringsGet[2].equals("getSubtask")) {
                        String query = httpExchange.getRequestURI().getQuery();
                        String[] querys = query.split("&");
                        int querySubtaskID = Integer.parseInt(querys[0]);
                        int queryEpicID = Integer.parseInt(querys[1]);
                        response = fileBackedTasksManager.getSubTaskById(queryEpicID, querySubtaskID, fileBackedTasksManager).toString();
                    } else if (splitStringsGet[2].equals("history")) {
                        response = fileBackedTasksManager.getInMemoryHistoryManager().getHistory().toString();
                    } else if (splitStringsGet[2].equals("alltasks")) {
                        response = fileBackedTasksManager.getTasksList().toString() + " " + fileBackedTasksManager.getEpicsList().toString();
                    } else if (splitStringsGet[2].equals("taskStatus")) {
                        int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                        response = fileBackedTasksManager.getTaskStatusById(query);
                    } else if (splitStringsGet[2].equals("epicStatus")) {
                        int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                        response = fileBackedTasksManager.getEpicStatusById(query);
                    }
                }
                break;
            case "POST":
                String[] splitStringsPost = path.split("/");
                if (splitStringsPost[2].equals("task")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Task task = gson.fromJson(body, Task.class);
                    response = task.toString();
                } else if (splitStringsPost[2].equals("epic")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Epic epic = gson.fromJson(body, Epic.class);
                    response = epic.toString();
                } else if (splitStringsPost[2].equals("subtask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Subtask subtask = gson.fromJson(body, Subtask.class);
                    response = subtask.toString();
                } else if (splitStringsPost[2].equals("newTask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Task task = gson.fromJson(body, Task.class);
                    response = task.toString();
                } else if (splitStringsPost[2].equals("newEpic")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Epic epic = gson.fromJson(body, Epic.class);
                    response = epic.toString();
                } else if (splitStringsPost[2].equals("newSubtask")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    final Subtask subtask = gson.fromJson(body, Subtask.class);
                    response = subtask.toString();
                }
                break;
            case "DELETE":
                String[] splitStringsDelete = path.split("/");
                if (splitStringsDelete[2].equals("allTask")) {
                    response = fileBackedTasksManager.clearAllTasks(fileBackedTasksManager).toString();
                } else if (splitStringsDelete[2].equals("allEpic")) {
                    response = fileBackedTasksManager.clearAllEpic(fileBackedTasksManager).toString();
                } else if (splitStringsDelete[2].equals("allSubtask")) {
                    int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                    response = fileBackedTasksManager.clearAllSubTasks(query, fileBackedTasksManager).toString();
                } else if (splitStringsDelete[2].equals("task")) {
                    int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                    response = fileBackedTasksManager.clearTaskById(query, fileBackedTasksManager).toString();
                } else if (splitStringsDelete[2].equals("epic")) {
                    int query = Integer.parseInt(httpExchange.getRequestURI().getQuery());
                    response = fileBackedTasksManager.clearEpicById(query, fileBackedTasksManager).toString();
                } else if (splitStringsDelete[2].equals("subtask")) {
                    String query = httpExchange.getRequestURI().getQuery();
                    String[] querys = query.split("&");
                    int querySubtaskID = Integer.parseInt(querys[0]);
                    int queryEpicID = Integer.parseInt(querys[1]);
                    response = fileBackedTasksManager.clearSubTaskById(fileBackedTasksManager.getEpicsList().get(queryEpicID), querySubtaskID, fileBackedTasksManager).toString();
                }
                break;
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}


