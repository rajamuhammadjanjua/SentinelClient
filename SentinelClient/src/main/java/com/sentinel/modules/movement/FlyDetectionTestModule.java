package com.sentinel.modules.movement;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class FlyDetectionTestModule extends SentinelModule {
    public FlyDetectionTestModule() {
        super("fly_detection_test", "Fly Detection Test", "Simulates vertical sustain and gravity-curve offsets to test flight checks.", Category.MOVEMENT);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 10 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        double metric = (randomizeScenarios ? (0.8 + random.nextDouble() * 0.8) : 1.0) * (0.25 + factor);
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.30;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        emitTestEvent(logger, "FLY_VERTICAL_SIM", String.format("vertDelta=%.3f b/t", metric), status, false);
    }
}
