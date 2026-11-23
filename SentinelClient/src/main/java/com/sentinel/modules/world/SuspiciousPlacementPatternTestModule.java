package com.sentinel.modules.world;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class SuspiciousPlacementPatternTestModule extends SentinelModule {
    public SuspiciousPlacementPatternTestModule() {
        super("suspicious_placement_test", "Placement Pattern Test", "Evaluates repetitive placement offsets and rotation snap consistency.", Category.WORLD);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        double rate = 4.0 + factor * 18.0;
        emitTestEvent(logger, "PLACE_PATTERN_SIM", String.format("angleVariance=%.2f", rate), status, false);
    }
}
