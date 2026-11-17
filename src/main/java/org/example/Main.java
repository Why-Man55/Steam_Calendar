package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String API_KEY = "B439AE0DA40D11C862E02B63ACB32FE7";

    public static void main(String[] args) {
        // Пример ввода: можно указать как никнейм (например, "gabelogannewell"), так и ID ("76561197960287930")
        String userInput = "raftalis94581";

        try {
            String steamId = getSteamId(userInput);
            if (steamId == null) {
                System.out.println("Ошибка: Не удалось найти пользователя или определить его SteamID.");
                return;
            }

            System.out.println("Используем SteamID64: " + steamId);
            System.out.println("-------------------------------------");
            fetchAndPrintPlayerSummary(steamId);

        } catch (Exception e) {
            System.err.println("Произошла ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Определяет SteamID64. Если на входе никнейм, запрашивает ID через ResolveVanityURL.
     */
    private static String getSteamId(String input) throws Exception {
        // Проверяем, состоит ли ввод только из 17 цифр (стандартный SteamID64)
        if (input.matches("^\\d{17}$")) {
            return input;
        }

        // Если это буквенный никнейм, делаем запрос к ResolveVanityURL
        String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + API_KEY + "&vanityurl=" + input;

        String responseBody = sendGetRequest(url);

        // Извлекаем steamid с помощью регулярного выражения
        Pattern pattern = Pattern.compile("\"steamid\":\\s*\"(\\d+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * Получает данные профиля и выводит их в консоль.
     */
    private static void fetchAndPrintPlayerSummary(String steamId) throws Exception {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002//?key=" + API_KEY + "&steamids=" + steamId;

        String responseBody = sendGetRequest(url);

        // Простой парсинг ключевых полей с помощью регулярных выражений
        String nickname = extractJsonValue(responseBody, "personaname");
        String profileUrl = extractJsonValue(responseBody, "profileurl");
        String avatarUrl = extractJsonValue(responseBody, "avatarfull"); // полная версия аватара
        String visibility = extractJsonValue(responseBody, "communityvisibilitystate");

        // Вывод данных в консоль
        System.out.println("Данные профиля Steam:");
        System.out.println("Никнейм: " + nickname);
        System.out.println("Ссылка на профиль: " + profileUrl);
        System.out.println("Ссылка на аватар: " + avatarUrl);

        if ("3".equals(visibility)) {
            System.out.println("Статус приватности: Открытый профиль (Public)");
        } else {
            System.out.println("Статус приватности: Скрытый профиль (Private/Friends Only)");
        }
    }
    /**
     * Утилитарный метод для отправки HTTP GET запроса
     */
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