package com.sentinel.modules.misc;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;

public class EmergencyStopModule extends SentinelModule {
    public EmergencyStopModule() {
        super("emergency_stop", "Emergency Stop", "Immediately halts all active test simulations and clears active states.", Category.MISC);
    }

    @Override
    protected void onEnable(SentinelLogger logger) {
        this.enabled = false;
        SentinelClient.getInstance().getModuleManager().emergencyStopAll(logger);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        // Immediate halt
    }
}
