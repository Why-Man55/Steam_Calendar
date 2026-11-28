package org.example.domain.service;

import org.example.domain.models.AccountModel;
import org.example.domain.models.RegisterCodes;
import org.example.domain.models.SteamProfileDto;
import org.example.domain.repository.AccountRepository;
import org.example.domain.repository.SteamGateway;
import org.example.domain.utils.SteamUtils;

public class AuthService {
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final AccountRepository implAccountRepository;
    private final SteamGateway steamApiClient;

    public AuthService(AccountRepository implAccountRepository, SteamGateway steamApiClient) {
        this.implAccountRepository = implAccountRepository;
        this.steamApiClient = steamApiClient;
    }

    public RegisterCodes register(String id, String username, String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return RegisterCodes.WEAK_PASSWORD;
        }

        if (implAccountRepository.isExists(id)) {
            return RegisterCodes.EXISTS;
        }

        SteamProfileDto profile = steamApiClient.fetchPlayerSummary(id, null);
        if (profile == null) {
            return RegisterCodes.STEAM_NOT_FOUND;
        }

        String finalUsername = (username == null || username.isBlank())
                ? profile.getPersonaName()
                : username.trim();

        String hash = SteamUtils.makeHash(password);
        boolean saved = implAccountRepository.saveAccount(new AccountModel(id, finalUsername, hash)) == 1;

        return saved ? RegisterCodes.SUCCESS : RegisterCodes.SERVER_ERROR;
    }

    public boolean checkLogin(String id, String password) {
        if (id == null || password == null || !implAccountRepository.isExists(id)) {
            return false;
        }
        String hash = SteamUtils.makeHash(password);
        String checkHash = implAccountRepository.getHash(id);
        return checkHash != null && checkHash.equals(hash);
    }
}
