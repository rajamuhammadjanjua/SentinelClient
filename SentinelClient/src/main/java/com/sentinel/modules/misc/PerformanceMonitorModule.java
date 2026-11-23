package com.sentinel.modules.misc;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.DetectionStatus;
import com.sentinel.utils.PerformanceMonitor;

public class PerformanceMonitorModule extends SentinelModule {
    public PerformanceMonitorModule() {
        super("performance_monitor", "Performance Monitor", "Tracks client tick frame timing and JVM heap memory usage.", Category.MISC);
    }

    @Override
    protected void onTickSimulation(SentinelLogger logger) {
        if (ticksRunning % 20 == 0) {
            PerformanceMonitor pm = SentinelClient.getInstance().getPerformanceMonitor();
            String info = String.format("tick=%.2fms mem=%d/%dMB", pm.getLastTickMs(), pm.getUsedMemoryMb(), pm.getMaxMemoryMb());
            emitTestEvent(logger, "PERF_SAMPLE", info, DetectionStatus.NOT_DETECTED, false);
        }
    }
}
