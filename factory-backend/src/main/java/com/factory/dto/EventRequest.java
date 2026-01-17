package com.factory.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class EventRequest {
    private String eventId;
    private Instant eventTime;
    private Instant receivedTime;
    private String machineId;
    private long durationMs;
    private int defectCount;
    private String lineId;
    private String factoryId;
}
