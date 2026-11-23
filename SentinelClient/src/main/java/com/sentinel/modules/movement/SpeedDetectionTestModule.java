package com.sentinel.modules.movement;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class SpeedDetectionTestModule extends SentinelModule {
    public SpeedDetectionTestModule() {
        super("speed_detection_test", "Speed Detection Test", "Simulates horizontal displacement vectors to validate ground/air speed checks.", Category.MOVEMENT);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 10 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        double metric = (randomizeScenarios ? (0.8 + random.nextDouble() * 0.8) : 1.0) * (0.25 + factor);
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.30;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        emitTestEvent(logger, "SPEED_VECTOR_SIM", String.format("horizSpeed=%.3f b/t", metric), status, false);
    }
}
