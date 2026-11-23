package org.example.domain.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Utils {
    public static String makeHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
