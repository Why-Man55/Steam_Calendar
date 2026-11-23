package org.example.domain;

import org.example.dao.Dao;
import org.example.domain.models.AccountModel;
import org.example.domain.utils.Utils;

public class AuthService {
    Dao dao;
    public AuthService(Dao dao) {
        this.dao = dao;
    }

    public boolean isRegistered(String id) {
        return dao.isExists(id);
    }

    public boolean register(String id, String username, String password) {
        String hash = Utils.makeHash(password);

        dao.saveAccount(new AccountModel(id, username, hash));
        return false;
    }

    public boolean checkLogin(String id, String password) {
        if (dao.isExists(id)) {
            String hash = Utils.makeHash(password);
            String checkHash = dao.getHash(id);

            return checkHash.equals(hash);
        } else {
            return false;
        }
    }
}
