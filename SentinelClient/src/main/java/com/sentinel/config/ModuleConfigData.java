package com.sentinel.config;

import com.sentinel.testing.BehaviorProfile;

public class ModuleConfigData {
    public boolean enabled = false;
    public double intensity = 0.5;
    public int durationSeconds = 15;
    public boolean randomizeScenarios = true;
    public BehaviorProfile profile = BehaviorProfile.NORMAL_BASELINE;
    public int keybind = -1;
}
