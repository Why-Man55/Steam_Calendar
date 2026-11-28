package org.example.domain.service;

import org.example.data.persistence.ImplAvailabilityRepository;
import org.example.domain.models.TimeSlot;
import org.example.domain.models.CalendarCodes;
import org.example.domain.models.TimeSlotDto;

import java.time.*;
import java.util.List;

public class CalendarService {
    private final ImplAvailabilityRepository implAvailabilityRepository;

    public CalendarService(ImplAvailabilityRepository implAvailabilityRepository) {
        this.implAvailabilityRepository = implAvailabilityRepository;
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

            boolean saved = implAvailabilityRepository.addAvailability(accountId, start, end);
            return saved ? CalendarCodes.SUCCESS : CalendarCodes.DB_ERROR;

        } catch (DateTimeException e) {
            return CalendarCodes.PARSE_ERROR;
        }
    }

    public List<TimeSlotDto> getUserSlots(String accountId) {
        return implAvailabilityRepository.getUserAvailability(accountId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<TimeSlotDto> getFriendsSlots(String accountId) {
        return implAvailabilityRepository.getFriendsAvailability(accountId).stream()
                .map(this::toDto)
                .toList();
    }

    public CalendarCodes deleteSlot(int slotId, String accountId) {
        boolean deleted = implAvailabilityRepository.deleteAvailability(slotId, accountId);
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
