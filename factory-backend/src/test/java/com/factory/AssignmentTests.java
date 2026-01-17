package com.factory;

import com.factory.dto.BatchResponse;
import com.factory.dto.EventRequest;
import com.factory.dto.StatsResponse;
import com.factory.service.EventService;
import com.factory.service.StatsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssignmentTests {

    @Autowired EventService eventService;
    @Autowired StatsService statsService;
    @Autowired MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanDb() {
        mongoTemplate.dropCollection("machine_events");
    }

    private EventRequest baseEvent(String id, Instant eventTime, Instant receivedTime, int defects) {
        EventRequest e = new EventRequest();
        e.setEventId(id);
        e.setMachineId("M-001");
        e.setEventTime(eventTime);
        e.setReceivedTime(receivedTime);
        e.setDurationMs(1000);
        e.setDefectCount(defects);
        e.setLineId("L-01");
        e.setFactoryId("F01");
        return e;
    }

    // 1. Identical duplicate → deduped
    @Test @Order(1)
    void identicalDuplicateEventDeduped() {
        EventRequest e = baseEvent("E1", Instant.now(), Instant.now(), 0);

        eventService.ingestBatch(List.of(e));
        BatchResponse r = eventService.ingestBatch(List.of(e));

        assertEquals(1, r.getDeduped());
    }

    // 2. Different payload + newer receivedTime → update happens
    @Test @Order(2)
    void updateWhenNewerReceivedTime() {
        Instant now = Instant.now();

        EventRequest e1 = baseEvent("E2", now, now, 1);
        eventService.ingestBatch(List.of(e1));

        EventRequest e2 = baseEvent("E2", now, now.plusSeconds(60), 5);
        BatchResponse r = eventService.ingestBatch(List.of(e2));

        assertEquals(1, r.getUpdated());
    }

    // 3. Different payload + older receivedTime → ignored
    @Test @Order(3)
    void ignoreOlderReceivedTimeUpdate() {
        Instant now = Instant.now();

        EventRequest e1 = baseEvent("E3", now, now.plusSeconds(60), 1);
        eventService.ingestBatch(List.of(e1));

        EventRequest e2 = baseEvent("E3", now, now, 5);
        BatchResponse r = eventService.ingestBatch(List.of(e2));

        assertEquals(0, r.getUpdated());
        assertEquals(0, r.getDeduped());
    }

    // 4. Invalid duration rejected
    @Test @Order(4)
    void invalidDurationRejected() {
        EventRequest e = baseEvent("E4", Instant.now(), Instant.now(), 1);
        e.setDurationMs(-10);

        BatchResponse r = eventService.ingestBatch(List.of(e));

        assertEquals(1, r.getRejected());
        assertEquals("INVALID_DURATION", r.getRejections().get(0).getReason());
    }

    // 5. Future eventTime rejected
    @Test @Order(5)
    void futureEventTimeRejected() {
        EventRequest e = baseEvent("E5",
                Instant.now().plusSeconds(3600),
                Instant.now(), 1);

        BatchResponse r = eventService.ingestBatch(List.of(e));

        assertEquals(1, r.getRejected());
        assertEquals("FUTURE_EVENT_TIME", r.getRejections().get(0).getReason());
    }

    // 6. defectCount = -1 ignored in defect totals
    @Test @Order(6)
    void defectMinusOneIgnoredInStats() {
        Instant start = Instant.parse("2026-01-15T00:00:00Z");
        Instant end   = Instant.parse("2026-01-16T00:00:00Z");

        EventRequest e1 = baseEvent("E6", start.plusSeconds(10), Instant.now(), -1);
        EventRequest e2 = baseEvent("E7", start.plusSeconds(20), Instant.now(), 3);

        eventService.ingestBatch(List.of(e1, e2));

        StatsResponse stats = statsService.stats("M-001", start, end);

        assertEquals(2, stats.getEventsCount());
        assertEquals(3, stats.getDefectsCount()); // -1 ignored
    }

    // 7. start inclusive, end exclusive correctness
    @Test @Order(7)
    void startInclusiveEndExclusiveWindow() {
        Instant start = Instant.parse("2026-01-15T10:00:00Z");
        Instant end   = Instant.parse("2026-01-15T11:00:00Z");

        EventRequest e1 = baseEvent("E8", start, Instant.now(), 1);
        EventRequest e2 = baseEvent("E9", end, Instant.now(), 1); // should be excluded

        eventService.ingestBatch(List.of(e1, e2));

        StatsResponse stats = statsService.stats("M-001", start, end);

        assertEquals(1, stats.getEventsCount());
    }

    // 8. Thread safety test: concurrent ingestion
    @Test @Order(8)
    void concurrentIngestionThreadSafe() throws Exception {
        EventRequest e = baseEvent("E10", Instant.now(), Instant.now(), 1);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<Void> task = () -> {
            eventService.ingestBatch(List.of(e));
            return null;
        };

        executor.invokeAll(List.of(task, task, task, task, task));
        executor.shutdown();

        // Only one event should exist → rest deduped
        BatchResponse r = eventService.ingestBatch(List.of(e));
        assertEquals(1, r.getDeduped());
    }
}
