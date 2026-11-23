package com.sentinel.modules.player;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

public class InventoryAutomationTestModule extends SentinelModule {
    public InventoryAutomationTestModule() {
        super("inventory_automation_test", "Inventory Automation Test", "Simulates container slot-click intervals to test auto-armor/chest-stealer checks.", Category.PLAYER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 12 != 0) return;
        double factor = intensity * profile.getIntensityMultiplier();
        boolean flagged = profile != BehaviorProfile.NORMAL_BASELINE && factor > 0.35;
        DetectionStatus status = flagged ? DetectionStatus.DETECTED : DetectionStatus.NOT_DETECTED;
        int ms = Math.max(15, (int) (180 - factor * 140));
        emitTestEvent(logger, "INV_AUTOMATION_SIM", String.format("slotIntervalMs=%d", ms), status, false);
    }
}
