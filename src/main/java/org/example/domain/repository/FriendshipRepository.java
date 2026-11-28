package org.example.domain.repository;

import java.util.List;

public interface FriendshipRepository {
    void saveFriends(String accountId, List<String> registeredFriendIds);
    List<String> getFriendIds(String accountId);
}
