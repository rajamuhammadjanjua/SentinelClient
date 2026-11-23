package com.sentinel.modules.misc;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;

public class DebugModeModule extends SentinelModule {
    public DebugModeModule() {
        super("debug_mode", "Debug Mode", "Enables verbose diagnostic logging for simulation ticks and state transitions.", Category.MISC);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 20 == 0) {
            emitTestEvent(logger, "DEBUG_HEARTBEAT", "tick=" + ticksRunning, DetectionStatus.NOT_DETECTED, false);
        }
    }
}
