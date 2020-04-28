package lavka.drools.integrationservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lavka.drools.utils.JsonBodyHandler;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IntegrationService {
    void importProductBase();

    void importCategories();

    default <T> Supplier<T> sendGetHttpRequest(URIBuilder builder, Class<T> clazz) {
        HttpClient client = HttpClient.newHttpClient();

        Supplier<T> body = null;

        try {
            URI uri = builder.build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            body = client.send(request,
                    new JsonBodyHandler<>(clazz))
                    .body();

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return body;
    }

    default <T> List<T> sendListGetHttpRequest(URIBuilder builder, Class<T> clazz) {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        List<T> body = null;

        try {
            URI uri = builder.build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            Type type = TypeToken.getParameterized(ArrayList.class, clazz).getType();

            String res = client.send(request,
                    HttpResponse.BodyHandlers.ofString())
                    .body();

            body = gson.fromJson(res, type);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return body;
    }

    default <T> List<T> sendListPostHttpRequest(URIBuilder builder, Object requestBody, Class<T> clazz) {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        List<T> body = null;

        try {
            URI uri = builder.build();

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                    .build();

            Type type = TypeToken.getParameterized(ArrayList.class, clazz).getType();

            String res = client.send(request,
                    HttpResponse.BodyHandlers.ofString())
                    .body();

            body = gson.fromJson(res, type);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return body;
    }
}
