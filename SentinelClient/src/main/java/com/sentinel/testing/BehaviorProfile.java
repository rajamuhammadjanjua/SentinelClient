package com.sentinel.testing;

public enum BehaviorProfile {
    NORMAL_BASELINE("Normal Baseline", 0.25, false),
    SUSPICIOUS("Suspicious Profile", 0.65, false),
    EXTREME("Extreme Profile", 1.00, true);

    private final String label;
    private final double intensityMultiplier;
    private final boolean requiresWarning;

    BehaviorProfile(String label, double intensityMultiplier, boolean requiresWarning) {
        this.label = label;
        this.intensityMultiplier = intensityMultiplier;
        this.requiresWarning = requiresWarning;
    }

    public String getLabel() { return label; }
    public double getIntensityMultiplier() { return intensityMultiplier; }
    public boolean requiresWarning() { return requiresWarning; }
}
