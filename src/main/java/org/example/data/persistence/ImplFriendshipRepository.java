package org.example.data.persistence;

import org.example.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImplFriendshipRepository {
    public void saveFriends(String accountId, List<String> registeredFriendIds) {
        if (registeredFriendIds == null || registeredFriendIds.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO friendships (account_id, friend_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            for (String friendId : registeredFriendIds) {
                if (!accountId.equals(friendId)) {
                    st.setString(1, accountId);
                    st.setString(2, friendId);
                    st.addBatch();
                }
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFriendIds(String accountId) {
        String sql = "SELECT friend_id FROM friendships WHERE account_id = ?";
        List<String> friends = new ArrayList<>();

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, accountId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    friends.add(rs.getString("friend_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }
}
