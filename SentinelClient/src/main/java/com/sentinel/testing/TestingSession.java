package com.sentinel.testing;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TestingSession {
    private final String sessionId;
    private final Instant startedAt;
    private Instant endedAt;
    private boolean active;
    private final List<TestEventRecord> events = new ArrayList<>();
    private final List<String> serverResponses = new ArrayList<>();

    public TestingSession() {
        this.sessionId = UUID.randomUUID().toString().substring(0, 8);
        this.startedAt = Instant.now();
        this.active = true;
    }

    public synchronized void addEvent(TestEventRecord record) {
        events.add(record);
    }

    public synchronized void addServerResponse(String response) {
        serverResponses.add(response);
    }

    public synchronized void stop() {
        this.active = false;
        this.endedAt = Instant.now();
    }

    public String getSessionId() { return sessionId; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getEndedAt() { return endedAt; }
    public boolean isActive() { return active; }

    public synchronized List<TestEventRecord> getEvents() {
        return Collections.unmodifiableList(new ArrayList<>(events));
    }

    public synchronized List<String> getServerResponses() {
        return Collections.unmodifiableList(new ArrayList<>(serverResponses));
    }

    public synchronized int countByStatus(DetectionStatus target) {
        int count = 0;
        for (TestEventRecord r : events) {
            if (r.getStatus() == target) count++;
        }
        return count;
    }

    public synchronized int countFalsePositives() {
        int count = 0;
        for (TestEventRecord r : events) {
            if (r.isFalsePositive()) count++;
        }
        return count;
    }
}
