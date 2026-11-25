package org.example.view.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsForSteam {

    private static final Pattern PROFILE_URL_PATTERN = Pattern.compile("steamcommunity\\.com/profiles/(\\d{17})");
    private static final Pattern ID_URL_PATTERN = Pattern.compile("steamcommunity\\.com/id/([^/]+)");
    private static final Pattern STEAM_ID_64_PATTERN = Pattern.compile("^\\d{17}$");

    public static boolean isCorrectId(String id) {
        return id == null || !id.matches("17\\d{15}");
    }

    public static ParsedInput parse(String rawInput) {
        if (rawInput == null || rawInput.isBlank()) {
            return null;
        }
        String input = rawInput.trim();

        Matcher profileMatcher = PROFILE_URL_PATTERN.matcher(input);
        if (profileMatcher.find()) {
            return new ParsedInput(profileMatcher.group(1), true);
        }

        Matcher idMatcher = ID_URL_PATTERN.matcher(input);
        if (idMatcher.find()) {
            return new ParsedInput(idMatcher.group(1), false);
        }

        if (STEAM_ID_64_PATTERN.matcher(input).matches()) {
            return new ParsedInput(input, true);
        }

        return new ParsedInput(input, false);
    }

    public record ParsedInput(String value, boolean isSteamId64) {}
}
