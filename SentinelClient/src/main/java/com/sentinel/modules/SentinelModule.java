package com.sentinel.modules;

import com.sentinel.config.ModuleConfigData;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.testing.BehaviorProfile;
import com.sentinel.testing.DetectionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class SentinelModule {
    private final String id;
    private final String name;
    private final String description;
    private final Category category;

    protected boolean enabled = false;
    protected double intensity = 0.5;
    protected int durationSeconds = 15;
    protected boolean randomizeScenarios = true;
    protected BehaviorProfile profile = BehaviorProfile.NORMAL_BASELINE;
    protected int keybind = -1;
    protected int ticksRunning = 0;

    protected final Random random = new Random();
    protected final List<String> moduleLogs = new ArrayList<>();

    protected SentinelModule(String id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }

    public boolean isEnabled() { return enabled; }
    public double getIntensity() { return intensity; }
    public int getDurationSeconds() { return durationSeconds; }
    public boolean isRandomizeScenarios() { return randomizeScenarios; }
    public BehaviorProfile getProfile() { return profile; }
    public int getKeybind() { return keybind; }

    public void setIntensity(double intensity) { this.intensity = Math.max(0.05, Math.min(1.0, intensity)); }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = Math.max(1, Math.min(600, durationSeconds)); }
    public void setRandomizeScenarios(boolean randomizeScenarios) { this.randomizeScenarios = randomizeScenarios; }
    public void setProfile(BehaviorProfile profile) { this.profile = profile; }
    public void setKeybind(int keybind) { this.keybind = keybind; }

    public synchronized void toggle(SentinelLogger logger) {
        setEnabled(!this.enabled, logger);
    }

    public synchronized void setEnabled(boolean nextState, SentinelLogger logger) {
        if (this.enabled == nextState) return;
        this.enabled = nextState;
        this.ticksRunning = 0;
        if (nextState) {
            onEnable(logger);
        } else {
            onDisable(logger);
        }
    }

    public synchronized void startSimulation(SentinelLogger logger) {
        this.ticksRunning = 0;
        setEnabled(true, logger);
        emitTestEvent(logger, "SIMULATION_START",
                "Profile=" + profile.getLabel() + " intensity=" + String.format("%.2f", intensity),
                DetectionStatus.TEST_COMPLETED, false);
    }

    public synchronized void stopSimulation(SentinelLogger logger) {
        setEnabled(false, logger);
        emitTestEvent(logger, "SIMULATION_STOP", "Controlled test stopped", DetectionStatus.TEST_COMPLETED, false);
    }

    public synchronized void resetToDefaults(SentinelLogger logger) {
        setEnabled(false, logger);
        this.intensity = 0.5;
        this.durationSeconds = 15;
        this.randomizeScenarios = true;
        this.profile = BehaviorProfile.NORMAL_BASELINE;
        this.keybind = -1;
        this.ticksRunning = 0;
        appendModuleLog("Module settings reset to defaults.");
        if (logger != null) {
            logger.record(name, "MODULE_RESET", "Restored default parameters", DetectionStatus.TEST_COMPLETED, false);
        }
    }

    public void applyConfig(ModuleConfigData data) {
        if (data == null) return;
        this.enabled = data.enabled;
        this.intensity = Math.max(0.05, Math.min(1.0, data.intensity));
        this.durationSeconds = Math.max(1, Math.min(600, data.durationSeconds));
        this.randomizeScenarios = data.randomizeScenarios;
        this.profile = data.profile != null ? data.profile : BehaviorProfile.NORMAL_BASELINE;
        this.keybind = data.keybind;
    }

    public ModuleConfigData toConfigData() {
        ModuleConfigData d = new ModuleConfigData();
        d.enabled = this.enabled;
        d.intensity = this.intensity;
        d.durationSeconds = this.durationSeconds;
        d.randomizeScenarios = this.randomizeScenarios;
        d.profile = this.profile;
        d.keybind = this.keybind;
        return d;
    }

    public synchronized void tick(SentinelLogger logger) {
        if (!enabled) return;
        ticksRunning++;
        int maxTicks = durationSeconds * 20;
        if (ticksRunning >= maxTicks) {
            emitTestEvent(logger, "DURATION_REACHED",
                    "Completed " + durationSeconds + "s test window",
                    DetectionStatus.TEST_COMPLETED, false);
            setEnabled(false, logger);
            return;
        }
        onTickSimulation(logger);
    }

    protected void emitTestEvent(SentinelLogger logger, String eventType, String detail, DetectionStatus status, boolean falsePositive) {
        appendModuleLog(eventType + ": " + detail + " [" + status.getLabel() + "]");
        if (logger != null) {
            logger.record(name, eventType, detail, status, falsePositive);
        }
    }

    protected void appendModuleLog(String entry) {
        moduleLogs.add(0, entry);
        if (moduleLogs.size() > 50) {
            moduleLogs.remove(moduleLogs.size() - 1);
        }
    }

    public synchronized List<String> getModuleLogs() {
        return Collections.unmodifiableList(new ArrayList<>(moduleLogs));
    }

    protected void onEnable(SentinelLogger logger) {
        appendModuleLog("Enabled (" + profile.getLabel() + ")");
    }

    protected void onDisable(SentinelLogger logger) {
        appendModuleLog("Disabled");
    }

    protected abstract void onTickSimulation(SentinelLogger logger);
}
