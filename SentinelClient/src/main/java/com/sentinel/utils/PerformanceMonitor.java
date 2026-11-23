package com.sentinel.utils;

public class PerformanceMonitor {
    private long lastTickNanos = System.nanoTime();
    private double lastTickMs = 0.0;
    private double avgTickMs = 0.0;
    private int tickCount = 0;

    public void onClientTickStart() {
        lastTickNanos = System.nanoTime();
    }

    public void onClientTickEnd() {
        long elapsed = System.nanoTime() - lastTickNanos;
        lastTickMs = elapsed / 1_000_000.0;
        tickCount++;
        avgTickMs = ((avgTickMs * (tickCount - 1)) + lastTickMs) / tickCount;
    }

    public double getLastTickMs() { return lastTickMs; }
    public double getAvgTickMs() { return avgTickMs; }
    public long getUsedMemoryMb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / (1024L * 1024L);
    }
    public long getMaxMemoryMb() {
        return Runtime.getRuntime().maxMemory() / (1024L * 1024L);
    }
}
