package org.example;

public class MainService {

    MainDao dao;

    public MainService(MainDao dao) {
        this.dao = dao;
    }
    public int checkAuth(String username, String password) {
        if (dao.saveAccount() != -1) {

        }
    }
}
