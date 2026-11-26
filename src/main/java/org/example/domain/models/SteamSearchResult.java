package org.example.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SteamSearchResult {

    SteamProfileDto profile;
    String errorMessage;

    public static SteamSearchResult success(SteamProfileDto profile) {
        return new SteamSearchResult(profile, null);
    }

    public static SteamSearchResult failure(String errorMessage) {
        return new SteamSearchResult(null, errorMessage);
    }

    public boolean isSuccess() {
        return profile != null;
    }
}
