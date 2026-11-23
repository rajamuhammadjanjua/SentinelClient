package com.sentinel.modules.combat;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class AimAssistTestModule extends SentinelModule {
    public AimAssistTestModule() {
        super("aim_assist_test", "Aim Assist Test", "Simulates controlled yaw/pitch smoothing deltas to test aim-assist heuristics.", Category.COMBAT);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 10 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        double valA = (randomizeScenarios ? random.nextDouble() : 0.5) * (1.0 + factor * 18.0);
        double valB = (randomizeScenarios ? random.nextDouble() : 0.5) * (1.0 + factor * 9.0);
        boolean suspicious = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = suspicious ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        boolean fp = profile == BehaviorProfile.NORMAL_BASELINE && factor > 0.20 && random.nextDouble() < 0.08;
        String detail = String.format("yawDelta=%.2f deg pitchDelta=%.2f deg", valA, valB);
        emitTestEvent(logger, "AIM_DELTA_SIM", detail, status, fp);
    }
}
