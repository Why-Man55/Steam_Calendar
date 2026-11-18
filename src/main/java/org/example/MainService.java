package org.example;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class MainService {

    MainDao dao;

    public MainService(MainDao dao) {
        this.dao = dao;
    }
    public int checkAuth(String id, String password) {
        String hash = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        AccountModel account = dao.getAccount(id);
        if (account == null) {
            return -1;
        } else if (account.passwordHash.equals(hash)) {
            return -2;
        } else {
            return 0;
        }
    }
}
