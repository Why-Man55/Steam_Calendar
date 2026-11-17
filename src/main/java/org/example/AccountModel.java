package org.example;

public class AccountModel {
    String id;
    String name;
    String passwordHash;

    public AccountModel(String id, String name,String passwordHash) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
    }
}
