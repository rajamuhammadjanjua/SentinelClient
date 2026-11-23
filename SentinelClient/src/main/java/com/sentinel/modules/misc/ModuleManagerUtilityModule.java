package com.sentinel.modules.misc;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;

public class ModuleManagerUtilityModule extends SentinelModule {
    public ModuleManagerUtilityModule() {
        super("module_manager_util", "Module Manager", "Reports active module count and registry integrity across categories.", Category.MISC);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 30 == 0) {
            int active = SentinelClient.getInstance().getModuleManager().getEnabledModules().size();
            emitTestEvent(logger, "MODULE_REGISTRY", "activeModules=" + active, DetectionStatus.NOT_DETECTED, false);
        }
    }
}
