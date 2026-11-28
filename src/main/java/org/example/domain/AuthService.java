package org.example.domain;

import org.example.dao.AccountDao;
import org.example.dao.models.AccountModel;
import org.example.data.SteamApiClient;
import org.example.domain.models.RegisterCodes;
import org.example.domain.models.SteamProfileDto;
import org.example.domain.utils.DomainUtilsForSteam;

public class AuthService {
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final AccountDao accountDao;
    private final SteamApiClient steamApiClient;

    public AuthService(AccountDao accountDao, SteamApiClient steamApiClient) {
        this.accountDao = accountDao;
        this.steamApiClient = steamApiClient;
    }

    public RegisterCodes register(String id, String username, String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return RegisterCodes.WEAK_PASSWORD;
        }

        if (accountDao.isExists(id)) {
            return RegisterCodes.EXISTS;
        }

        SteamProfileDto profile = steamApiClient.fetchPlayerSummary(id, null);
        if (profile == null) {
            return RegisterCodes.STEAM_NOT_FOUND;
        }

        String finalUsername = (username == null || username.isBlank())
                ? profile.getPersonaName()
                : username.trim();

        String hash = DomainUtilsForSteam.makeHash(password);
        boolean saved = accountDao.saveAccount(new AccountModel(id, finalUsername, hash)) == 1;

        return saved ? RegisterCodes.SUCCESS : RegisterCodes.SERVER_ERROR;
    }

    public boolean checkLogin(String id, String password) {
        if (id == null || password == null || !accountDao.isExists(id)) {
            return false;
        }
        String hash = DomainUtilsForSteam.makeHash(password);
        String checkHash = accountDao.getHash(id);
        return checkHash != null && checkHash.equals(hash);
    }
}
