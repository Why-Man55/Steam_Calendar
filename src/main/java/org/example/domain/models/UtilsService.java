package org.example.domain.models;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class UtilsService {
    public static String makeHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
