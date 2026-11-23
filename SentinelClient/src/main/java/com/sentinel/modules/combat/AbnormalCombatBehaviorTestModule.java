package com.sentinel.modules.combat;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class AbnormalCombatBehaviorTestModule extends SentinelModule {
    public AbnormalCombatBehaviorTestModule() {
        super("abnormal_combat_test", "Abnormal Combat Test", "Combines movement-during-swing and look-consistency telemetry for combat checks.", Category.COMBAT);
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
        String detail = String.format("swingWhileMoving=%b", valA, valB);
        emitTestEvent(logger, "COMBAT_STATE_SIM", detail, status, fp);
    }
}
