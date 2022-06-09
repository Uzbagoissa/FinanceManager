package manager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new Handler(taskManager));
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    static class Handler implements HttpHandler {
        private TaskManager taskManager;
        private Gson gson = new Gson();
        private static final String GET = "GET";
        private static final String POST = "POST";
        private static final String DELETE = "DELETE";

        public Handler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        protected String readText(HttpExchange httpExchange) throws IOException {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            gson.fromJson(body, Task.class);
            return body;
        }

        protected int getQuery(HttpExchange httpExchange) {
            return Integer.parseInt(httpExchange.getRequestURI().getQuery());
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.sendResponseHeaders(200, 0);
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();

            switch (method) {
                case GET:
                    if (path.equals("/tasks/")){
                        response = taskManager.getPrioritizedTasksList().toString();
                    } else {
                        String[] splitStringsGet = path.split("/");
                        switch (splitStringsGet[2]) {
                            case "taskList":
                                response = taskManager.getTasksList().toString();
                                break;
                            case "epicList":
                                response = taskManager.getEpicsList().toString();
                                break;
                            case "subTaskList":
                                response = taskManager.getEpicsList().get(getQuery(httpExchange)).getSubTasksList().toString();
                                break;
                            case "getAnytask":
                                response = taskManager.getAnyTaskById(getQuery(httpExchange)).toString();
                                break;
                            case "getSubtask":
                                String queryg = httpExchange.getRequestURI().getQuery();
                                String[] querys = queryg.split("&");
                                int querySubtaskID = Integer.parseInt(querys[0]);
                                int queryEpicID = Integer.parseInt(querys[1]);
                                response = taskManager.getSubTaskById(queryEpicID, querySubtaskID).toString();
                                break;
                            case "history":
                                response = taskManager.getInMemoryHistoryManager().getHistory().toString();
                                break;
                            case "alltasks":
                                response = taskManager.getTasksList().toString() + " " + taskManager.getEpicsList().toString();
                                break;
                            case "taskStatus":
                                response = taskManager.getTaskStatusById(getQuery(httpExchange));
                                break;
                            case "epicStatus":
                                response = taskManager.getEpicStatusById(getQuery(httpExchange));
                                break;
                            default:
                                response = "Вы использовали какой-то другой метод!";
                        }
                    }
                    break;
                case POST:
                    String[] splitStringsPost = path.split("/");
                    switch (splitStringsPost[2]) {
                        case "task": {
                            final Task task = gson.fromJson(readText(httpExchange), Task.class);
                            response = task.toString();
                            break;
                        }
                        case "epic": {
                            final Epic epic = gson.fromJson(readText(httpExchange), Epic.class);
                            response = epic.toString();
                            break;
                        }
                        case "subtask": {
                            final Subtask subtask = gson.fromJson(readText(httpExchange), Subtask.class);
                            response = subtask.toString();
                            break;
                        }
                        case "newTask": {
                            final Task task = gson.fromJson(readText(httpExchange), Task.class);
                            response = task.toString();
                            break;
                        }
                        case "newEpic": {
                            final Epic epic = gson.fromJson(readText(httpExchange), Epic.class);
                            response = epic.toString();
                            break;
                        }
                        case "newSubtask": {
                            final Subtask subtask = gson.fromJson(readText(httpExchange), Subtask.class);
                            response = subtask.toString();
                            break;
                        }
                        default:
                            response = "Вы использовали какой-то другой метод!";
                    }
                    break;
                case DELETE:
                    String[] splitStringsDelete = path.split("/");
                    switch (splitStringsDelete[2]) {
                        case "allTask":
                            response = taskManager.clearAllTasks().toString();
                            break;
                        case "allEpic":
                            response = taskManager.clearAllEpic().toString();
                            break;
                        case "allSubtask":
                            response = taskManager.clearAllSubTasks(getQuery(httpExchange)).toString();
                            break;
                        case "task":
                            response = taskManager.clearTaskById(getQuery(httpExchange)).toString();
                            break;
                        case "epic":
                            response = taskManager.clearEpicById(getQuery(httpExchange)).toString();
                            break;
                        case "subtask":
                            String queryg1 = httpExchange.getRequestURI().getQuery();
                            String[] querys = queryg1.split("&");
                            int querySubtaskID = Integer.parseInt(querys[0]);
                            int queryEpicID = Integer.parseInt(querys[1]);
                            response = taskManager.clearSubTaskById(taskManager.getEpicsList().get(queryEpicID), querySubtaskID).toString();
                            break;
                        default:
                            response = "Вы использовали какой-то другой метод!";
                    }
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}


