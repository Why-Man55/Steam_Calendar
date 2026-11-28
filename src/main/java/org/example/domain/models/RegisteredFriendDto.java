package org.example.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisteredFriendDto {
    private String steamId;
    private String personaName;
    private String avatarUrl;
    private boolean alreadyAdded;
}
