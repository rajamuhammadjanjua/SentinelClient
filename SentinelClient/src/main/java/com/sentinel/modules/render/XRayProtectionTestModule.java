package com.sentinel.modules.render;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;
import net.minecraft.client.MinecraftClient;

public class XRayProtectionTestModule extends SentinelModule {
    public XRayProtectionTestModule() {
        super("xray_protection_test", "X-Ray Protection Test", "Tests ore/block visibility sampling against authorized chunk/world state.", Category.RENDER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 15 != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int baseCount = (client.world != null) ? Math.max(1, client.world.getRegularEntityCount()) : 12;
        int scaled = (int) Math.max(1, baseCount * (0.5 + intensity * profile.getIntensityMultiplier()));
        emitTestEvent(logger, "XRAY_SAMPLE_SIM", String.format("blocksSampled=%d", scaled), DetectionStatus.NOT_DETECTED, false);
    }
}
