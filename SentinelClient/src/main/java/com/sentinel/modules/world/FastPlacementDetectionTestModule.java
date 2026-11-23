package com.sentinel.modules.world;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class FastPlacementDetectionTestModule extends SentinelModule {
    public FastPlacementDetectionTestModule() {
        super("fast_placement_test", "Fast Placement Test", "Simulates rapid block placement intervals to test CPS placement limits.", Category.WORLD);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        double rate = 4.0 + factor * 18.0;
        emitTestEvent(logger, "FAST_PLACE_SIM", String.format("intervalMs=%d", rate), status, false);
    }
}
