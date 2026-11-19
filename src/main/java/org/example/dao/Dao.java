package org.example.dao;

import org.example.domain.models.AccountModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dao {
    public int saveAccount(AccountModel model){
        String sql = "Insert into account (id, username, password_hash) values (?, ?, ?)";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, model.getId());
            st.setString(1, model.getUsername());
            st.setString(3, model.getPasswordHash());
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }
}
