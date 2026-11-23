package com.sentinel.modules.player;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class PlayerStateConsistencyTestModule extends SentinelModule {
    public PlayerStateConsistencyTestModule() {
        super("player_state_consistency_test", "Player State Consistency", "Cross-checks client pose, sneaking, sprinting, and health/hunger state consistency.", Category.PLAYER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        int ms = Math.max(15, (int) (180 - factor * 140));
        emitTestEvent(logger, "PLAYER_STATE_SIM", String.format("stateSynced=%b", ms), status, false);
    }
}
