package com.sentinel.modules.misc;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;

public class ConfigurationManagerModule extends SentinelModule {
    public ConfigurationManagerModule() {
        super("config_manager", "Configuration Manager", "Saves and reloads Sentinel configuration in the Fabric config directory.", Category.MISC);
    }

    @Override
    protected void onEnable(SentinelLogger logger) {
        super.onEnable(logger);
        SentinelClient.getInstance().saveConfiguration();
        emitTestEvent(logger, "CONFIG_SAVED", "Persisted configuration to config/sentinel-client.json", DetectionStatus.TEST_COMPLETED, false);
        this.enabled = false;
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        // One-shot action module
    }
}
