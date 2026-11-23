package com.sentinel.modules;

import com.sentinel.config.ModuleConfigData;
import com.sentinel.config.SentinelConfig;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.combat.*;
import com.sentinel.modules.movement.*;
import com.sentinel.modules.render.*;
import com.sentinel.modules.world.*;
import com.sentinel.modules.player.*;
import com.sentinel.modules.misc.*;

import java.util.*;

public class ModuleManager {
    private final List<SentinelModule> modules = new ArrayList<>();
    private final Map<String, SentinelModule> byId = new LinkedHashMap<>();

    public ModuleManager() {
        registerDefaults();
    }

    private void registerDefaults() {
        // Combat
        register(new AimAssistTestModule());
        register(new KillAuraPatternTestModule());
        register(new AutoClickerPatternTestModule());
        register(new ReachValidationTestModule());
        register(new SuspiciousAttackTimingTestModule());
        register(new TargetSelectionPatternTestModule());
        register(new AbnormalCombatBehaviorTestModule());

        // Movement
        register(new SpeedDetectionTestModule());
        register(new FlyDetectionTestModule());
        register(new FlightStateValidationTestModule());
        register(new NoSlowDetectionTestModule());
        register(new AntiKnockbackValidationTestModule());
        register(new TimerManipulationTestModule());
        register(new AbnormalMovementPatternTestModule());
        register(new InvalidMovementPacketTestModule());

        // Render
        register(new EspDetectionTestModule());
        register(new EntityVisibilityTestModule());
        register(new XRayProtectionTestModule());
        register(new BlockVisibilityValidationTestModule());
        register(new EntityTrackingDiagnosticsTestModule());
        register(new ChunkVisibilityDiagnosticsTestModule());

        // World
        register(new ScaffoldPlacementValidationTestModule());
        register(new SuspiciousPlacementPatternTestModule());
        register(new InvalidPlacementDetectionTestModule());
        register(new FastPlacementDetectionTestModule());
        register(new BlockInteractionValidationTestModule());

        // Player
        register(new InventoryAutomationTestModule());
        register(new AbnormalInventoryPatternTestModule());
        register(new ItemUseMovementValidationTestModule());
        register(new PlayerStateConsistencyTestModule());

        // Misc
        register(new TestEventLoggerModule());
        register(new DebugModeModule());
        register(new ConfigurationManagerModule());
        register(new ModuleManagerUtilityModule());
        register(new EmergencyStopModule());
        register(new TestingSessionManagerModule());
        register(new PerformanceMonitorModule());
    }

    public void register(SentinelModule module) {
        modules.add(module);
        byId.put(module.getId(), module);
    }

    public List<SentinelModule> getAllModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<SentinelModule> getByCategory(Category category) {
        List<SentinelModule> out = new ArrayList<>();
        for (SentinelModule m : modules) {
            if (m.getCategory() == category) out.add(m);
        }
        return out;
    }

    public List<SentinelModule> getEnabledModules() {
        List<SentinelModule> out = new ArrayList<>();
        for (SentinelModule m : modules) {
            if (m.isEnabled()) out.add(m);
        }
        return out;
    }

    public void tickAll(SentinelLogger logger) {
        for (SentinelModule m : modules) {
            if (m.isEnabled()) {
                m.tick(logger);
            }
        }
    }

    public void emergencyStopAll(SentinelLogger logger) {
        for (SentinelModule m : modules) {
            if (m.isEnabled()) {
                m.setEnabled(false, logger);
            }
        }
        if (logger != null) {
            logger.record("EmergencyStop", "EMERGENCY_STOP", "All active simulation modules halted",
                    com.sentinel.testing.DetectionStatus.TEST_COMPLETED, false);
        }
    }

    public void applyConfig(SentinelConfig config) {
        if (config == null || config.modules == null) return;
        for (SentinelModule m : modules) {
            ModuleConfigData data = config.modules.get(m.getId());
            if (data != null) {
                m.applyConfig(data);
            }
        }
    }

    public void writeToConfig(SentinelConfig config) {
        if (config == null) return;
        config.modules.clear();
        for (SentinelModule m : modules) {
            config.modules.put(m.getId(), m.toConfigData());
        }
    }
}
