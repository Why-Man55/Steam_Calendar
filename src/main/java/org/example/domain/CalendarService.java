package org.example.domain;

import org.example.dao.AvailabilityDao;
import org.example.dao.models.TimeSlot;
import org.example.domain.models.CalendarCodes;
import org.example.domain.models.TimeSlotDto;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarService {
    private final AvailabilityDao availabilityDao;

    public CalendarService(AvailabilityDao availabilityDao) {
        this.availabilityDao = availabilityDao;
    }

    public CalendarCodes addSlot(String accountId, String startStr, String endStr, String zoneIdStr) {
        if (startStr == null || endStr == null || startStr.isBlank() || endStr.isBlank()) {
            return CalendarCodes.PARSE_ERROR;
        }

        try {
            ZoneId zoneId = (zoneIdStr == null || zoneIdStr.isBlank())
                    ? ZoneId.systemDefault()
                    : ZoneId.of(zoneIdStr);

            OffsetDateTime start = LocalDateTime.parse(startStr).atZone(zoneId).toOffsetDateTime();
            OffsetDateTime end = LocalDateTime.parse(endStr).atZone(zoneId).toOffsetDateTime();

            if (!end.isAfter(start) || end.isBefore(OffsetDateTime.now())) {
                return CalendarCodes.INVALID_TIME_RANGE;
            }

            boolean saved = availabilityDao.addAvailability(accountId, start, end);
            return saved ? CalendarCodes.SUCCESS : CalendarCodes.DB_ERROR;

        } catch (DateTimeException e) {
            return CalendarCodes.PARSE_ERROR;
        }
    }

    public List<TimeSlotDto> getUserSlots(String accountId) {
        return availabilityDao.getUserAvailability(accountId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<TimeSlotDto> getFriendsSlots(String accountId) {
        return availabilityDao.getFriendsAvailability(accountId).stream()
                .map(this::toDto)
                .toList();
    }

    public CalendarCodes deleteSlot(int slotId, String accountId) {
        boolean deleted = availabilityDao.deleteAvailability(slotId, accountId);
        return deleted ? CalendarCodes.SUCCESS : CalendarCodes.SLOT_NOT_FOUND;
    }

    private TimeSlotDto toDto(TimeSlot slot) {
        return new TimeSlotDto(
                slot.getId(),
                slot.getAccountId(),
                slot.getStart(),
                slot.getEnd()
        );
    }
}
