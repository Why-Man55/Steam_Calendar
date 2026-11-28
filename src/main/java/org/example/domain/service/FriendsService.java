package org.example.domain.service;

import org.example.domain.models.FriendsCodes;
import org.example.domain.models.RegisteredFriendDto;
import org.example.domain.models.SteamProfileDto;
import org.example.domain.repository.AccountRepository;
import org.example.domain.repository.FriendshipRepository;
import org.example.domain.repository.SteamGateway;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsService {

    private final SteamGateway steamApiClient;
    private final AccountRepository implAccountRepository;
    private final FriendshipRepository implFriendshipRepository;

    public FriendsService(SteamGateway steamApiClient, AccountRepository implAccountRepository, FriendshipRepository implFriendshipRepository) {
        this.steamApiClient = steamApiClient;
        this.implAccountRepository = implAccountRepository;
        this.implFriendshipRepository = implFriendshipRepository;
    }

    public List<RegisteredFriendDto> getAvailableSteamFriends(String userSteamId) {
        List<String> steamFriendIds = steamApiClient.fetchFriendIds(userSteamId);
        if (steamFriendIds == null || steamFriendIds.isEmpty()) {
            return List.of();
        }

        List<String> registeredIds = steamFriendIds.stream()
                .filter(implAccountRepository::isExists)
                .toList();

        if (registeredIds.isEmpty()) {
            return List.of();
        }

        Set<String> alreadySaved = new HashSet<>(implFriendshipRepository.getFriendIds(userSteamId));
        List<SteamProfileDto> profiles = steamApiClient.fetchPlayerSummaries(registeredIds);

        return profiles.stream()
                .map(p -> new RegisteredFriendDto(
                        p.getSteamId(),
                        p.getPersonaName(),
                        p.getAvatarFullUrl(),
                        alreadySaved.contains(p.getSteamId())
                ))
                .toList();
    }

    public FriendsCodes saveSelectedFriends(String userSteamId, List<String> selectedFriendIds) {
        if (selectedFriendIds == null || selectedFriendIds.isEmpty()) {
            return FriendsCodes.EMPTY_SELECTION;
        }

        List<String> validIds = selectedFriendIds.stream()
                .filter(id -> !id.equals(userSteamId))
                .filter(implAccountRepository::isExists)
                .toList();

        if (validIds.isEmpty()) {
            return FriendsCodes.EMPTY_SELECTION;
        }

        implFriendshipRepository.saveFriends(userSteamId, validIds);
        return FriendsCodes.SUCCESS;
    }
}
