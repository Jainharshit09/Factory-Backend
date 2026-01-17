package com.factory.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class StatsResponse {
    private String machineId;
    private Instant start;
    private Instant end;
    private long eventsCount;
    private long defectsCount;
    private double avgDefectRate;
    private String status;
}
