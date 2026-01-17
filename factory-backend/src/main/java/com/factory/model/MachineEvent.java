package com.factory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "machine_events")
public class MachineEvent {
    @Id
    private String eventId;
    private Instant eventTime;
    private Instant receivedTime;
    private String machineId;
    private long durationMs;
    private int defectCount;
    private String payloadHash;
    private String lineId;
    private String factoryId;
}
