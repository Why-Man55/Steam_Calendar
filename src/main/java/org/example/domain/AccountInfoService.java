package org.example.domain;

import org.example.dao.Dao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountInfoService {

    Dao dao;
    public AccountInfoService(Dao dao) {
        this.dao = dao;
    }
    private final SteamApiClient steamApiClient;

    public AccountInfoService(Dao dao, SteamApiClient steamApiClient) {
        this.dao = dao;
        this.steamApiClient = steamApiClient;
    }

    public SteamProfileDto getPublicProfile(String steamId) {
        if (steamId == null || steamId.isBlank()) {
            return null;
        }

        // Получаем базовую информацию из Steam API
        SteamProfileDto profile = steamApiClient.fetchPlayerSummary(steamId.trim());

        if (profile == null) {
            return null;
        }

        // При необходимости можно дополнить объект данными из вашей БД
        // например: boolean isRegistered = dao.existsBySteamId(steamId);
        // profile.setRegisteredInApp(isRegistered);

        return profile;
    }
    private static String getSteamId(String input) throws Exception {
        // Проверяем, состоит ли ввод только из 17 цифр (стандартный SteamID64)
        if (input.matches("^\\d{17}$")) {
            return input;
        }

        String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + System.getenv("API_KEY") + "&vanityurl=" + input;

        String responseBody = sendGetRequest(url);

        // Извлекаем steamid с помощью регулярного выражения
        Pattern pattern = Pattern.compile("\"steamid\":\\s*\"(\\d+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private static void fetchAndPrintPlayerSummary(String steamId) throws Exception {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002//?key=" + System.getenv("API_KEY") + "&steamids=" + steamId;

        String responseBody = sendGetRequest(url);

        String nickname = extractJsonValue(responseBody, "personaname");
        String profileUrl = extractJsonValue(responseBody, "profileurl");
        String avatarUrl = extractJsonValue(responseBody, "avatarfull"); // полная версия аватара
        String visibility = extractJsonValue(responseBody, "communityvisibilitystate");


        if ("3".equals(visibility)) {
            System.out.println("Статус приватности: Открытый профиль (Public)");
        } else {
            System.out.println("Статус приватности: Скрытый профиль (Private/Friends Only)");
        }
    }

    private static String sendGetRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Вспомогательный метод для извлечения строковых значений из JSON без сторонних библиотек
     */
    private static String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        // Если значение числовое (без кавычек), пробуем найти его так
        pattern = Pattern.compile("\"" + key + "\":\\s*(\\d+)");
        matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Не найдено";
    }
}
