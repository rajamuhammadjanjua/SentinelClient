package com.sentinel.modules.render;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;
import net.minecraft.client.MinecraftClient;

public class EntityTrackingDiagnosticsTestModule extends SentinelModule {
    public EntityTrackingDiagnosticsTestModule() {
        super("entity_tracking_diag", "Entity Tracking Diag", "Monitors tracked entity updates and interpolation latency on the client.", Category.RENDER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 15 != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int baseCount = (client.world != null) ? Math.max(1, client.world.getRegularEntityCount()) : 12;
        int scaled = (int) Math.max(1, baseCount * (0.5 + intensity * profile.getIntensityMultiplier()));
        emitTestEvent(logger, "TRACKING_DIAG", String.format("trackedEntities=%d", scaled), DetectionStatus.NOT_DETECTED, false);
    }
}
