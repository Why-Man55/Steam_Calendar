package org.example.domain;

import org.example.dao.AccountDao;
import org.example.dao.models.AccountModel;
import org.example.domain.utils.DomainUtilsForSteam;

public class AuthService {
    AccountDao accountDao;
    public AuthService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public boolean isRegistered(String id) {
        return accountDao.isExists(id);
    }

    public boolean register(String id, String username, String password) {
        String hash = DomainUtilsForSteam.makeHash(password);
        return accountDao.saveAccount(new AccountModel(id, username, hash)) == 1;
    }

    public boolean checkLogin(String id, String password) {
        if (accountDao.isExists(id)) {
            String hash = DomainUtilsForSteam.makeHash(password);
            String checkHash = accountDao.getHash(id);

            return checkHash.equals(hash);
        } else {
            return false;
        }
    }
}
