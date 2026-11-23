package org.example.domain;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SteamApiClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey;

    public SteamApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public SteamProfileDto fetchPlayerSummary(String steamId) {
        String url = String.format(
                "https://steampowered.com",
                apiKey, steamId
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            // Ваш метод парсинга JSON. Если массив players пустой — вернет null
            return parseJsonToDto(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запросе к Steam API", e);
        }
    }

    private SteamProfileDto parseJsonToDto(String json) {
        // Логика парсинга JSON в SteamProfileDto (или возврат null, если игрок не найден)
        return null;
    }
}
