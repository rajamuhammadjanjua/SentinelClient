package com.sentinel.modules.render;

import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;
import net.minecraft.client.MinecraftClient;

public class EntityVisibilityTestModule extends SentinelModule {
    public EntityVisibilityTestModule() {
        super("entity_visibility_test", "Entity Visibility Test", "Validates frustum and line-of-sight entity culling state without exposing hidden data.", Category.RENDER);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 15 != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int baseCount = (client.world != null) ? Math.max(1, client.world.getRegularEntityCount()) : 12;
        int scaled = (int) Math.max(1, baseCount * (0.5 + intensity * profile.getIntensityMultiplier()));
        emitTestEvent(logger, "ENTITY_VIS_CHECK", String.format("visibleCount=%d", scaled), DetectionStatus.NOT_DETECTED, false);
    }
}
