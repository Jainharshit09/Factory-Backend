package com.factory.controller;

import com.factory.dto.*;
import com.factory.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping("/events/batch")
    public BatchResponse ingest(@RequestBody List<EventRequest> events) {
        return service.ingestBatch(events);
    }
}
