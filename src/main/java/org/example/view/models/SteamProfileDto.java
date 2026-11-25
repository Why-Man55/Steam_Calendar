package org.example.view.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SteamProfileDto {
    private String steamId;
    private String personaName;
    private String avatarFullUrl;
    private String profileUrl;
    private String realName;
    private String countryCode;
    private int communityVisibilityState;
    private long timeCreated;
    private List<String> friendIds;

    public boolean isPublic() {
        return communityVisibilityState == 3;
    }
}
