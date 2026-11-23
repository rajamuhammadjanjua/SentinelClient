package com.sentinel.testing;

public enum DetectionStatus {
    NOT_DETECTED("Not Detected", 0xFF4ADE80),
    DETECTED("Detected", 0xFFF87171),
    TEST_FAILED("Test Failed", 0xFFFBBF24),
    TEST_COMPLETED("Test Completed", 0xFF60A5FA);

    private final String label;
    private final int colorArgb;

    DetectionStatus(String label, int colorArgb) {
        this.label = label;
        this.colorArgb = colorArgb;
    }

    public String getLabel() { return label; }
    public int getColorArgb() { return colorArgb; }
}
