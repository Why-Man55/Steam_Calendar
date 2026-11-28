package org.example.domain.repository;

import org.example.domain.models.TimeSlot;
import java.time.OffsetDateTime;
import java.util.List;

public interface AvailabilityRepository {
    boolean addAvailability(String accountId, OffsetDateTime start, OffsetDateTime end);
    List<TimeSlot> getUserAvailability(String accountId);
    List<TimeSlot> getFriendsAvailability(String accountId);
    boolean deleteAvailability(int slotId, String accountId);
}
