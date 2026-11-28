package org.example.data.persistence;

import org.example.DatabaseManager;
import org.example.domain.models.AccountModel;
import org.example.domain.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImplAccountRepository implements AccountRepository {
    @Override
    public int saveAccount(AccountModel model){
        String sql = "Insert into account (id, username, password_hash) values (?, ?, ?)";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, model.getId());
            st.setString(2, model.getUsername());
            st.setString(3, model.getPasswordHash());
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean isExists(String id) {
        String sql = "Select id from account where id = (?)";

        try( Connection con = DatabaseManager.getConnection();
        PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, id);
            try(ResultSet rs = st.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getHash(String id) {
        String sql = "Select password_hash from account where id = (?)";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
