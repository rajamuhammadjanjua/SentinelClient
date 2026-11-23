package com.sentinel.gui;

import com.sentinel.SentinelClient;
import com.sentinel.logging.SentinelLogger;
import com.sentinel.testing.DetectionStatus;
import com.sentinel.testing.TestEventRecord;
import com.sentinel.testing.TestingSession;
import com.sentinel.utils.PerformanceMonitor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class SentinelDashboardScreen extends Screen {
    private final Screen parent;

    public SentinelDashboardScreen(Screen parent) {
        super(Text.literal("Sentinel Anti-Cheat Testing Dashboard"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        addDrawableChild(ButtonWidget.builder(Text.literal("New Session"),
                btn -> SentinelClient.getInstance().getLogger().startNewSession())
                .dimensions(cx - 170, this.height - 28, 105, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Emergency Stop"),
                btn -> SentinelClient.getInstance().getModuleManager().emergencyStopAll(SentinelClient.getInstance().getLogger()))
                .dimensions(cx - 55, this.height - 28, 110, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Back to GUI"),
                btn -> {
                    if (client != null) client.setScreen(parent);
                }).dimensions(cx + 65, this.height - 28, 105, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xE6090D16);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFFF8FAFC);

        SentinelLogger logger = SentinelClient.getInstance().getLogger();
        TestingSession session = logger.getCurrentSession();
        PerformanceMonitor perf = SentinelClient.getInstance().getPerformanceMonitor();
        int activeModules = SentinelClient.getInstance().getModuleManager().getEnabledModules().size();

        int leftX = 18;
        int y = 34;

        context.drawText(this.textRenderer, "Session ID: " + session.getSessionId() + (session.isActive() ? " [ACTIVE]" : " [STOPPED]"), leftX, y, 0xFF38BDF8, false);
        context.drawText(this.textRenderer, "Active Simulations: " + activeModules + " | Total Events: " + session.getEvents().size(), leftX, y + 12, 0xFFE2E8F0, false);
        context.drawText(this.textRenderer, String.format("Not Detected: %d | Detected: %d | Failed: %d | Completed: %d | FP: %d",
                session.countByStatus(DetectionStatus.NOT_DETECTED),
                session.countByStatus(DetectionStatus.DETECTED),
                session.countByStatus(DetectionStatus.TEST_FAILED),
                session.countByStatus(DetectionStatus.TEST_COMPLETED),
                session.countFalsePositives()), leftX, y + 24, 0xFFCBD5E1, false);
        context.drawText(this.textRenderer, String.format("Perf: Tick %.2fms (Avg %.2fms) | Heap %d/%dMB | Sessions: %d",
                perf.getLastTickMs(), perf.getAvgTickMs(), perf.getUsedMemoryMb(), perf.getMaxMemoryMb(), logger.getSessionHistory().size()),
                leftX, y + 36, 0xFF94A3B8, false);

        context.drawText(this.textRenderer, "Note: Detections reflect simulated client telemetry unless confirmed by an authorized server response.",
                leftX, y + 48, 0xFFFBBF24, false);

        int eventY = y + 66;
        List<TestEventRecord> events = session.getEvents();
        int startIdx = Math.max(0, events.size() - 12);
        for (int i = events.size() - 1; i >= startIdx; i--) {
            TestEventRecord rec = events.get(i);
            context.drawTextWithShadow(this.textRenderer, rec.formatLine(), leftX, eventY, rec.getStatus().getColorArgb());
            eventY += 12;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
