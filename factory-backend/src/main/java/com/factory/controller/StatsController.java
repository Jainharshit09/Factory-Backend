
package com.factory.controller;

import com.factory.dto.*;
import com.factory.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;

    @GetMapping
    public StatsResponse stats(@RequestParam String machineId,
                               @RequestParam Instant start,
                               @RequestParam Instant end) {
        return service.stats(machineId,start,end);
    }

    @GetMapping("/top-defect-lines")
    public List<TopLineResponse> topLines(@RequestParam String factoryId,
                                          @RequestParam Instant from,
                                          @RequestParam Instant to,
                                          @RequestParam(defaultValue="10") int limit) {
        return service.topLines(factoryId,from,to,limit);
    }
}
