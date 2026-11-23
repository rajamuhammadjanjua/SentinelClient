package com.sentinel.modules.misc;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;

public class TestingSessionManagerModule extends SentinelModule {
    public TestingSessionManagerModule() {
        super("session_manager", "Testing Session Manager", "Starts a fresh testing session and archives the previous session.", Category.MISC);
    }

    @Override
    protected void onEnable(SentinelLogger logger) {
        this.enabled = false;
        var session = SentinelClient.getInstance().getLogger().startNewSession();
        emitTestEvent(logger, "SESSION_ROTATED", "New session=" + session.getSessionId(), DetectionStatus.TEST_COMPLETED, false);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        // One-shot rotation
    }
}
