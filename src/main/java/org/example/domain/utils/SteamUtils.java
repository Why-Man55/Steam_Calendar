package org.example.domain.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamUtils {

    private static final Pattern PROFILE_URL_PATTERN = Pattern.compile("steamcommunity\\.com/profiles/(\\d{17})");
    private static final Pattern ID_URL_PATTERN = Pattern.compile("steamcommunity\\.com/id/([^/]+)");
    private static final Pattern STEAM_ID_64_PATTERN = Pattern.compile("^\\d{17}$");

    public static String makeHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    public static SteamUtils.ParsedInput parse(String rawInput) {
        if (rawInput == null || rawInput.isBlank()) {
            return null;
        }
        String input = rawInput.trim();

        Matcher profileMatcher = PROFILE_URL_PATTERN.matcher(input);
        if (profileMatcher.find()) {
            return new SteamUtils.ParsedInput(profileMatcher.group(1), true);
        }

        Matcher idMatcher = ID_URL_PATTERN.matcher(input);
        if (idMatcher.find()) {
            return new SteamUtils.ParsedInput(idMatcher.group(1), false);
        }

        if (STEAM_ID_64_PATTERN.matcher(input).matches()) {
            return new SteamUtils.ParsedInput(input, true);
        }

        return new SteamUtils.ParsedInput(input, false);
    }

    public record ParsedInput(String value, boolean isSteamId64) {}
}
