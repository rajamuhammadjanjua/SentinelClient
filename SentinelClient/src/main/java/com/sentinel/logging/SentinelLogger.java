package com.sentinel.logging;

import com.sentinel.testing.DetectionStatus;
import com.sentinel.testing.TestEventRecord;
import com.sentinel.testing.TestingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SentinelLogger {
    private static final Logger SLF4J = LoggerFactory.getLogger("SentinelClient");
    private static final int MAX_HISTORY_SESSIONS = 25;

    private final List<TestingSession> history = new ArrayList<>();
    private TestingSession currentSession;

    public SentinelLogger() {
        startNewSession();
    }

    public synchronized TestingSession startNewSession() {
        if (currentSession != null && currentSession.isActive()) {
            currentSession.stop();
        }
        TestingSession next = new TestingSession();
        currentSession = next;
        history.add(0, next);
        while (history.size() > MAX_HISTORY_SESSIONS) {
            history.remove(history.size() - 1);
        }
        SLF4J.info("Started Sentinel testing session {}", next.getSessionId());
        return next;
    }

    public synchronized void stopCurrentSession() {
        if (currentSession != null && currentSession.isActive()) {
            currentSession.stop();
            SLF4J.info("Stopped Sentinel testing session {}", currentSession.getSessionId());
        }
    }

    public synchronized TestingSession getCurrentSession() {
        if (currentSession == null) {
            startNewSession();
        }
        return currentSession;
    }

    public synchronized List<TestingSession> getSessionHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    public synchronized void record(String moduleName, String eventType, String detail, DetectionStatus status, boolean falsePositive) {
        TestingSession session = getCurrentSession();
        TestEventRecord record = new TestEventRecord(moduleName, eventType, detail, status, falsePositive);
        session.addEvent(record);
        SLF4J.debug(record.formatLine());
    }

    public synchronized void recordServerResponse(String response) {
        getCurrentSession().addServerResponse(response);
        SLF4J.info("Server telemetry response: {}", response);
    }

    public synchronized void clearLogs() {
        startNewSession();
    }
}
