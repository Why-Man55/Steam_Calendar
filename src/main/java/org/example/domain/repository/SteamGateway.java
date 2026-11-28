package org.example.domain.repository;


import org.example.domain.models.SteamProfileDto;
import java.util.List;

public interface SteamGateway {
    boolean hasApiKey();
    String resolveVanityUrl(String vanityUrl);
    SteamProfileDto fetchPlayerSummary(String steamId64, List<String> friendIds);
    List<SteamProfileDto> fetchPlayerSummaries(List<String> steamIds);
    List<String> fetchFriendIds(String steamId64);
}
