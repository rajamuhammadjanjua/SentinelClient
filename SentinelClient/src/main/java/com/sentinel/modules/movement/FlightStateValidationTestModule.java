package com.sentinel.modules.movement;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class FlightStateValidationTestModule extends SentinelModule {
    public FlightStateValidationTestModule() {
        super("flight_state_validation_test", "Flight State Validation", "Cross-checks client airborne flags against vertical velocity and ground proximity.", Category.MOVEMENT);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 10 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        double metric = (randomizeScenarios ? (0.8 + random.nextDouble() * 0.8) : 1.0) * (0.25 + factor);
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.30;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        emitTestEvent(logger, "FLIGHT_STATE_CHECK", String.format("airborneFlag=%b", metric), status, false);
    }
}
