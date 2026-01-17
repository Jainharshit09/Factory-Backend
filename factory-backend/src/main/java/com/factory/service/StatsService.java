package com.factory.service;

import com.factory.dto.StatsResponse;
import com.factory.dto.TopLineResponse;
import com.factory.model.MachineEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final MongoTemplate template;

    // ===== Endpoint 1: Machine Stats =====
    public StatsResponse stats(String machineId, Instant start, Instant end) {

        List<MachineEvent> events = template.find(
                query(where("machineId").is(machineId)
                        .and("eventTime").gte(start).lt(end)),
                MachineEvent.class
        );

        long eventsCount = events.size();

        long defects = events.stream()
                .filter(e -> e.getDefectCount() != -1)
                .mapToLong(MachineEvent::getDefectCount)
                .sum();

        double hours = (end.toEpochMilli() - start.toEpochMilli()) / 3600000.0;
        double avg = defects / hours;

        StatsResponse r = new StatsResponse();
        r.setMachineId(machineId);
        r.setStart(start);
        r.setEnd(end);
        r.setEventsCount(eventsCount);
        r.setDefectsCount(defects);
        r.setAvgDefectRate(avg);
        r.setStatus(avg < 2.0 ? "Healthy" : "Warning");

        return r;
    }

    // ===== Endpoint 2: Top Defect Lines =====
    public List<TopLineResponse> topLines(String factoryId,
                                          Instant start,
                                          Instant end,
                                          int limit) {

        Aggregation agg = newAggregation(

                // Match factory + time window
                match(where("factoryId").is(factoryId)
                        .and("eventTime").gte(start).lt(end)),

                // Group by lineId
                group("lineId")
                        // sum defects ONLY if defectCount != -1
                        .sum(
                                ConditionalOperators
                                        .when(Criteria.where("defectCount").ne(-1))
                                        .thenValueOf("defectCount")
                                        .otherwise(0)
                        ).as("totalDefects")
                        .count().as("eventCount"),

                // Project final fields
                project("totalDefects", "eventCount")
                        .and("_id").as("lineId")
                        .andExpression(
                                "round((totalDefects / eventCount) * 100, 2)"
                        ).as("defectsPercent"),

                sort(Sort.Direction.DESC, "totalDefects"),
                limit(limit)
        );

        return template
                .aggregate(agg, MachineEvent.class, TopLineResponse.class)
                .getMappedResults();
    }
}
