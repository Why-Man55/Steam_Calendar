package org.example.domain.repository;

import org.example.domain.models.AccountModel;

public interface AccountRepository {
    int saveAccount(AccountModel model);
    boolean isExists(String id);
    String getHash(String id);
}
