package com.factory.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class BatchResponse {
    private int accepted;
    private int deduped;
    private int updated;
    private int rejected;
    private List<Rejection> rejections = new ArrayList<>();

    @Data
    public static class Rejection {
        private String eventId;
        private String reason;
    }
}
