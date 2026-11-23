package com.sentinel;

import com.sentinel.config.SentinelConfig;
import com.sentinel.events.KeybindHandler;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.ModuleManager;
import com.sentinel.utils.PerformanceMonitor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SentinelClient implements ClientModInitializer {
    private static SentinelClient instance;

    private SentinelConfig config;
    private ModuleManager moduleManager;
    private SentinelLogger logger;
    private PerformanceMonitor performanceMonitor;

    @Override
    public void onInitializeClient() {
        instance = this;
        this.logger = new SentinelLogger();
        this.performanceMonitor = new PerformanceMonitor();
        this.config = SentinelConfig.load();
        this.moduleManager = new ModuleManager();
        this.moduleManager.applyConfig(this.config);

        KeybindHandler.register();

        ClientTickEvents.START_CLIENT_TICK.register(client -> performanceMonitor.onClientTickStart());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client != null && client.world != null) {
                moduleManager.tickAll(logger);
            }
            performanceMonitor.onClientTickEnd();
        });
    }

    public void saveConfiguration() {
        if (config != null && moduleManager != null) {
            moduleManager.writeToConfig(config);
            config.save();
        }
    }

    public static SentinelClient getInstance() {
        return instance;
    }

    public SentinelConfig getConfig() {
        return config;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SentinelLogger getLogger() {
        return logger;
    }

    public PerformanceMonitor getPerformanceMonitor() {
        return performanceMonitor;
    }
}
