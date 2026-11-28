package org.example.domain.service;

import org.example.data.persistence.ImplAccountRepository;
import org.example.data.SteamApiClient;
import org.example.domain.models.SteamProfileDto;
import org.example.domain.models.SteamSearchResult;
import org.example.domain.repository.AccountRepository;
import org.example.domain.repository.SteamGateway;
import org.example.domain.utils.SteamUtils;

import java.util.List;

public class AccountInfoService {

    private final AccountRepository implAccountRepository;
    private final SteamGateway steamApiClient;

    public AccountInfoService(AccountRepository implAccountRepository, SteamGateway steamApiClient) {
        this.implAccountRepository = implAccountRepository;
        this.steamApiClient = steamApiClient;
    }

    public SteamSearchResult searchProfile(String query) {
        if (!steamApiClient.hasApiKey()) {
            return SteamSearchResult.failure("Не задан STEAM_API_KEY в переменных окружения сервера");
        }

        SteamUtils.ParsedInput parsed = SteamUtils.parse(query);
        if (parsed == null) {
            return SteamSearchResult.failure("ID не найден");
        }

        String steamId64;
        if (parsed.isSteamId64()) {
            steamId64 = parsed.value();
        } else {
            steamId64 = steamApiClient.resolveVanityUrl(parsed.value());
        }

        if (steamId64 == null) {
            return SteamSearchResult.failure("ID не найден");
        }

        List<String> friendIds = steamApiClient.fetchFriendIds(steamId64);
        SteamProfileDto profile = steamApiClient.fetchPlayerSummary(steamId64, friendIds);

        if (profile == null) {
            return SteamSearchResult.failure("ID не найден");
        }

        return SteamSearchResult.success(profile);
    }
}
