package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainDao {
    public int saveAccount(AccountModel model){
        String sql = "Insert into account (id, username, password_hash) values (?, ?, ?)";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, model.id);
            st.setString(1, model.name);
            st.setString(3, model.passwordHash);
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }

    public AccountModel getAccount(String id) {
        String sql = "select id, username, password_hash from account where id=?";

        try( Connection con = DatabaseManager.getConnection();
        PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, id);
            try (ResultSet rs = st.executeQuery()) {
                String newId;
                String newUsername;
                String newHash;
                if (rs.next()){
                    newId = rs.getString("id");
                    newUsername = rs.getString("username");
                    newHash = rs.getString("password_hash");
                    return new AccountModel(newId, newUsername, newHash);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public int saveFriends(String userId, String[] friends_id) {
        String sql = "insert into friendships (account_id, friend_id) values (?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            for (String i : friends_id) {
                st.setString(1, userId);
                st.setString(2, i);
            }
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }
}
