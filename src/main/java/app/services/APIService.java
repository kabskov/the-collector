package app.services;

import app.exceptions.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String readAPI(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("User-Agent", "SchoolProjectRockCollectorApp/1.0 (cph-kn325@stud.ek.dk)")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException(502, "External API returned status " + response.statusCode());
            }
            return response.body();
        } catch (ApiException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(500, "Request was interrupted", e);
        } catch (Exception e) {
            throw new ApiException(502, "Could not reach external API", e);
        }
    }

    public <T> T convertFromJson(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new ApiException(502, "Could not read response from external API", e);
        }
    }

    public <T> T fetchAndConvert(String url, Class<T> tClass) {
        String json = readAPI(url);
        return convertFromJson(json, tClass);
    }
}