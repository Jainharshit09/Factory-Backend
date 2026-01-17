package com.factory.service;

import com.factory.dto.*;
import com.factory.model.MachineEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class EventService {

    private final MongoTemplate template;

    public BatchResponse ingestBatch(List<EventRequest> events) {

        BatchResponse response = new BatchResponse();

        for (EventRequest req : events) {

            // ===== Validation =====
            if (req.getDurationMs() < 0 || req.getDurationMs() > 21600000) {
                reject(response, req.getEventId(), "INVALID_DURATION");
                continue;
            }

            if (req.getEventTime().isAfter(Instant.now().plusSeconds(900))) {
                reject(response, req.getEventId(), "FUTURE_EVENT_TIME");
                continue;
            }

            String hash = hashPayload(req);

            // ===== Find existing event =====
            MachineEvent existing = template.findOne(
                    query(where("_id").is(req.getEventId())),
                    MachineEvent.class
            );

            // ===== Insert new =====
            if (existing == null) {
                template.insert(toEntity(req, hash));
                response.setAccepted(response.getAccepted() + 1);
                continue;
            }

            // ===== Deduplicate =====
            if (existing.getPayloadHash().equals(hash)) {
                response.setDeduped(response.getDeduped() + 1);
                continue;
            }

            // ===== Update if newer receivedTime =====
            if (req.getReceivedTime().isAfter(existing.getReceivedTime())) {
                template.save(toEntity(req, hash));
                response.setUpdated(response.getUpdated() + 1);
            }
        }

        return response;
    }

    // ===== Helper Methods =====

    private void reject(BatchResponse r, String id, String reason) {
        BatchResponse.Rejection rej = new BatchResponse.Rejection();
        rej.setEventId(id);
        rej.setReason(reason);
        r.setRejected(r.getRejected() + 1);
        r.getRejections().add(rej);
    }

    private MachineEvent toEntity(EventRequest r, String hash) {
        MachineEvent ev = new MachineEvent();
        ev.setEventId(r.getEventId());
        ev.setEventTime(r.getEventTime());
        ev.setReceivedTime(r.getReceivedTime());
        ev.setMachineId(r.getMachineId());
        ev.setDurationMs(r.getDurationMs());
        ev.setDefectCount(r.getDefectCount());
        ev.setPayloadHash(hash);
        ev.setLineId(r.getLineId());
        ev.setFactoryId(r.getFactoryId());
        return ev;
    }

    private String hashPayload(EventRequest r) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String raw = r.getEventId()
                    + r.getEventTime()
                    + r.getMachineId()
                    + r.getDurationMs()
                    + r.getDefectCount();

            return java.util.Base64.getEncoder()
                    .encodeToString(md.digest(raw.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
