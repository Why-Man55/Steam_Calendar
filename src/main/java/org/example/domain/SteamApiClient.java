package org.example.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.view.models.SteamProfileDto;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SteamApiClient {

    private static final String BASE_URL = "https://api.steampowered.com";
    private final String apiKey;
    private final HttpClient httpClient;

    public SteamApiClient(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("[SteamApiClient] ВНИМАНИЕ: Переменная окружения STEAM_API_KEY не задана!");
        }
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .proxy(ProxySelector.getDefault())
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String resolveVanityUrl(String vanityUrl) {
        String encoded = URLEncoder.encode(vanityUrl, StandardCharsets.UTF_8);
        String url = String.format("%s/ISteamUser/ResolveVanityURL/v1/?key=%s&vanityurl=%s",
                BASE_URL, apiKey, encoded);

        String body = sendGet(url);
        if (body == null) {
            return null;
        }

        JsonObject root = parseJsonObject(body);
        if (root == null || !root.has("response")) {
            return null;
        }
        JsonObject response = root.getAsJsonObject("response");
        if (response != null && response.has("success") && response.get("success").getAsInt() == 1) {
            return response.get("steamid").getAsString();
        }
        return null;
    }

    public SteamProfileDto fetchPlayerSummary(String steamId64, List<String> friendIds) {
        String url = String.format("%s/ISteamUser/GetPlayerSummaries/v2/?key=%s&steamids=%s",
                BASE_URL, apiKey, steamId64);

        String body = sendGet(url);
        if (body == null) {
            return null;
        }

        JsonObject root = parseJsonObject(body);
        if (root == null || !root.has("response")) {
            return null;
        }
        JsonObject response = root.getAsJsonObject("response");
        if (response == null) {
            return null;
        }

        JsonArray players = response.getAsJsonArray("players");
        if (players == null || players.isEmpty()) {
            return null;
        }

        JsonObject player = players.get(0).getAsJsonObject();

        return SteamProfileDto.builder()
                .steamId(getStringOrNull(player, "steamid"))
                .personaName(getStringOrNull(player, "personaname"))
                .avatarFullUrl(getStringOrNull(player, "avatarfull"))
                .profileUrl(getStringOrNull(player, "profileurl"))
                .realName(getStringOrNull(player, "realname"))
                .countryCode(getStringOrNull(player, "loccountrycode"))
                .communityVisibilityState(getIntOrDefault(player, "communityvisibilitystate", 1))
                .timeCreated(getLongOrDefault(player, "timecreated", 0L))
                .friendIds(friendIds)
                .build();
    }

    public List<String> fetchFriendIds(String steamId64) {
        List<String> friendIds = new ArrayList<>();
        String url = String.format("%s/ISteamUser/GetFriendList/v1/?key=%s&steamid=%s&relationship=friend",
                BASE_URL, apiKey, steamId64);

        String body = sendGet(url);
        if (body == null) {
            return friendIds;
        }

        JsonObject root = parseJsonObject(body);
        if (root == null || !root.has("friendslist")) {
            return friendIds;
        }

        JsonArray friends = root.getAsJsonObject("friendslist").getAsJsonArray("friends");
        if (friends != null) {
            for (JsonElement element : friends) {
                JsonObject friendObj = element.getAsJsonObject();
                if (friendObj.has("steamid")) {
                    friendIds.add(friendObj.get("steamid").getAsString());
                }
            }
        }
        return friendIds;
    }

    private String sendGet(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("[SteamApiClient] URL: " + url.replace(apiKey, "HIDDEN_KEY"));
            System.out.println("[SteamApiClient] Status: " + response.statusCode());
            System.out.println("[SteamApiClient] Body: " + response.body());

            if (response.statusCode() == 200) {
                return response.body();
            }
            return null;
        } catch (Exception e) {
            System.err.println("[SteamApiClient] Exception: " + e.getClass().getName() + " - " + e.getMessage());
            return null;
        }
    }

    private String getStringOrNull(JsonObject obj, String member) {
        return obj.has(member) ? obj.get(member).getAsString() : null;
    }

    private int getIntOrDefault(JsonObject obj, String member, int defaultValue) {
        return obj.has(member) ? obj.get(member).getAsInt() : defaultValue;
    }

    private long getLongOrDefault(JsonObject obj, String member, long defaultValue) {
        return obj.has(member) ? obj.get(member).getAsLong() : defaultValue;
    }

    private JsonObject parseJsonObject(String body) {
        if (body == null) {
            return null;
        }
        String trimmed = body.trim();
        // Если Steam вернул HTML (например, <html><head><title>Unauthorized</title>...), не падаем с MalformedJsonException
        if (!trimmed.startsWith("{")) {
            return null;
        }
        try {
            JsonElement element = JsonParser.parseString(trimmed);
            return element.isJsonObject() ? element.getAsJsonObject() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
