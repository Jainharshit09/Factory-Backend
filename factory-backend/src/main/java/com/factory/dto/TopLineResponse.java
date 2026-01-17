package com.factory.dto;

import lombok.Data;

@Data
public class TopLineResponse {
    private String lineId;
    private long totalDefects;
    private long eventCount;
    private double defectsPercent;
}
