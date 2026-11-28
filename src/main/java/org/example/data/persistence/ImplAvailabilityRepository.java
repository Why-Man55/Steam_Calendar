package org.example.data.persistence;

import org.example.DatabaseManager;
import org.example.domain.models.TimeSlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImplAvailabilityRepository {
    public boolean addAvailability(String accountId, OffsetDateTime start, OffsetDateTime end) {
        String sql = "INSERT INTO account_availability (account_id, available_time) VALUES (?, tstzrange(?, ?, '[)'))";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, accountId);
            st.setObject(2, start);
            st.setObject(3, end);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TimeSlot> getUserAvailability(String accountId) {
        String sql = """
                SELECT id, account_id, lower(available_time) AS start_time, upper(available_time) AS end_time
                FROM account_availability
                WHERE account_id = ? AND upper(available_time) > now()
                ORDER BY start_time
                """;

        return fetchSlotsByParam(sql, accountId);
    }

    public List<TimeSlot> getFriendsAvailability(String accountId) {
        String sql = """
                SELECT a.id, a.account_id, lower(a.available_time) AS start_time, upper(a.available_time) AS end_time
                FROM account_availability a
                JOIN friendships f ON f.friend_id = a.account_id
                WHERE f.account_id = ? AND upper(a.available_time) > now()
                ORDER BY start_time
                """;

        return fetchSlotsByParam(sql, accountId);
    }

    public boolean deleteAvailability(int slotId, String accountId) {
        String sql = "DELETE FROM account_availability WHERE id = ? AND account_id = ?";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, slotId);
            st.setString(2, accountId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<TimeSlot> fetchSlotsByParam(String sql, String accountId) {
        List<TimeSlot> slots = new ArrayList<>();
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, accountId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    slots.add(new TimeSlot(
                            rs.getInt("id"),
                            rs.getString("account_id"),
                            rs.getObject("start_time", OffsetDateTime.class),
                            rs.getObject("end_time", OffsetDateTime.class)
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
}
