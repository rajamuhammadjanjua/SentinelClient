package com.sentinel.modules.render;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;
import net.minecraft.client.MinecraftClient;

public class ChunkVisibilityDiagnosticsTestModule extends SentinelModule {
    public ChunkVisibilityDiagnosticsTestModule() {
        super("chunk_visibility_diag", "Chunk Visibility Diag", "Monitors loaded client chunk sections and render distance state.", Category.RENDER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 15 != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int baseCount = (client.world != null) ? Math.max(1, client.world.getRegularEntityCount()) : 12;
        int scaled = (int) Math.max(1, baseCount * (0.5 + intensity * profile.getIntensityMultiplier()));
        emitTestEvent(logger, "CHUNK_DIAG", String.format("loadedSections=%d", scaled), DetectionStatus.NOT_DETECTED, false);
    }
}
