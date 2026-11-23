package com.sentinel.modules.player;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class ItemUseMovementValidationTestModule extends SentinelModule {
    public ItemUseMovementValidationTestModule() {
        super("item_use_movement_test", "Item-Use Movement Test", "Validates movement speed and sprint state while consuming/using items.", Category.PLAYER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        int ms = Math.max(15, (int) (180 - factor * 140));
        emitTestEvent(logger, "ITEM_USE_MOVE_SIM", String.format("sprintingWhileUsing=%b", ms), status, false);
    }
}
