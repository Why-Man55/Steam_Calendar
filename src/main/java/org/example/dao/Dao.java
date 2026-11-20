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

    public boolean isExists(String id) {
        String sql = "Select id from account where id = (?)";

        try( Connection con = DatabaseManager.getConnection();
        PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, id);
            st.executeQuery();

            return st.getResultSet().wasNull();
        } catch (SQLException e) {
            return false;
        }
    }

    public String getHash(String id) {
        String sql = "Select password_hash from account where id = (?)";

        try(Connection con = DatabaseManager.getConnection();
        PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, id);

            return String.valueOf(st.getResultSet().next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
