package com.sentinel.modules.misc;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;

public class TestEventLoggerModule extends SentinelModule {
    public TestEventLoggerModule() {
        super("test_event_logger", "Test Event Logger", "Streams structured test event telemetry into the session log.", Category.MISC);
        this.enabled = true;
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 40 == 0 && logger != null) {
            int total = logger.getCurrentSession().getEvents().size();
            appendModuleLog("Active session event count: " + total);
        }
    }
}
