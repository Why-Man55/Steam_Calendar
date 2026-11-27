package org.example.domain;

import org.example.dao.AccountDao;
import org.example.data.SteamApiClient;
import org.example.domain.models.SteamProfileDto;
import org.example.domain.models.SteamSearchResult;
import org.example.domain.utils.DomainUtilsForSteam;

import java.util.List;

public class AccountInfoService {

    private final AccountDao accountDao;
    private final SteamApiClient steamApiClient;

    public AccountInfoService(AccountDao accountDao, SteamApiClient steamApiClient) {
        this.accountDao = accountDao;
        this.steamApiClient = steamApiClient;
    }

    public SteamSearchResult searchProfile(String query) {
        if (!steamApiClient.hasApiKey()) {
            return SteamSearchResult.failure("Не задан STEAM_API_KEY в переменных окружения сервера");
        }

        DomainUtilsForSteam.ParsedInput parsed = DomainUtilsForSteam.parse(query);
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
