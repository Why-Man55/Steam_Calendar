package org.example.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class TimeSlot {
    private int id;
    private String accountId;
    private OffsetDateTime start;
    private OffsetDateTime end;
}
