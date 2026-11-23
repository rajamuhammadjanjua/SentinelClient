package com.sentinel.modules.world;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class InvalidPlacementDetectionTestModule extends SentinelModule {
    public InvalidPlacementDetectionTestModule() {
        super("invalid_placement_test", "Invalid Placement Test", "Checks simulated block placements against reach and face-normal rules.", Category.WORLD);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        double rate = 4.0 + factor * 18.0;
        emitTestEvent(logger, "INVALID_PLACE_SIM", String.format("reachDist=%.2fm", rate), status, false);
    }
}
