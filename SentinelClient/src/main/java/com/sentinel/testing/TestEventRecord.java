package com.sentinel.testing;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TestEventRecord {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private final String timestamp;
    private final String moduleName;
    private final String eventType;
    private final String detail;
    private final DetectionStatus status;
    private final boolean falsePositive;

    public TestEventRecord(String moduleName, String eventType, String detail, DetectionStatus status, boolean falsePositive) {
        this.timestamp = FMT.format(Instant.now());
        this.moduleName = moduleName;
        this.eventType = eventType;
        this.detail = detail;
        this.status = status;
        this.falsePositive = falsePositive;
    }

    public String getTimestamp() { return timestamp; }
    public String getModuleName() { return moduleName; }
    public String getEventType() { return eventType; }
    public String getDetail() { return detail; }
    public DetectionStatus getStatus() { return status; }
    public boolean isFalsePositive() { return falsePositive; }

    public String formatLine() {
        return String.format("[%s] [%s] %s -> %s (%s)%s",
                timestamp, moduleName, eventType, detail, status.getLabel(),
                falsePositive ? " [FP]" : "");
    }
}
