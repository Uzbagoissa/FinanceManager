package manager;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient implements Serializable {

    private final String server;
    private final String apiToken;

    public KVTaskClient(final String server) {
        this.server = server;
        this.apiToken = register(server);
    }

    private String register(String server) {
        try {
            final HttpClient client = HttpClient.newHttpClient();
            final HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(server + "register"))
                    .GET()
                    .build();
            final HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("");
        }
    }

    public String load(String key) {
        try {
            final HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(server + "load/" + key + "?API_TOKEN=" + this.apiToken);
            final HttpRequest req = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            final HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("");
        }
    }

    public void put(String key, String json) {
        try {
            final HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(server + "save/" + key + "?API_TOKEN=" + this.apiToken);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(body)
                    .build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("");
        }
    }
}
