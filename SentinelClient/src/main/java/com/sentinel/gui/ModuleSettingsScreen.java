package com.sentinel.gui;

import com.sentinel.SentinelClient;
import com.sentinel.modules.SentinelModule;
import com.sentinel.testing.BehaviorProfile;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModuleSettingsScreen extends Screen {
    private final SentinelModule module;
    private final Screen parent;
    private boolean pendingExtremeConfirm = false;

    public ModuleSettingsScreen(SentinelModule module, Screen parent) {
        super(Text.literal(module.getName() + " Settings"));
        this.module = module;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int sy = 56;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("State: " + (module.isEnabled() ? "ENABLED" : "DISABLED")),
                btn -> {
                    module.toggle(SentinelClient.getInstance().getLogger());
                    btn.setMessage(Text.literal("State: " + (module.isEnabled() ? "ENABLED" : "DISABLED")));
                }).dimensions(cx - 160, sy, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Randomize: " + (module.isRandomizeScenarios() ? "ON" : "OFF")),
                btn -> {
                    module.setRandomizeScenarios(!module.isRandomizeScenarios());
                    btn.setMessage(Text.literal("Randomize: " + (module.isRandomizeScenarios() ? "ON" : "OFF")));
                }).dimensions(cx + 10, sy, 150, 20).build());

        sy += 26;
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Intensity - (0.05)"),
                btn -> module.setIntensity(module.getIntensity() - 0.10)).dimensions(cx - 160, sy, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Intensity +"),
                btn -> module.setIntensity(module.getIntensity() + 0.10)).dimensions(cx - 50, sy, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Duration +5s"),
                btn -> module.setDurationSeconds(module.getDurationSeconds() + 5)).dimensions(cx + 60, sy, 100, 20).build());

        sy += 26;
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Profile: Normal"),
                btn -> {
                    module.setProfile(BehaviorProfile.NORMAL_BASELINE);
                    pendingExtremeConfirm = false;
                }).dimensions(cx - 160, sy, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Profile: Suspicious"),
                btn -> {
                    module.setProfile(BehaviorProfile.SUSPICIOUS);
                    pendingExtremeConfirm = false;
                }).dimensions(cx - 50, sy, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Profile: Extreme"),
                btn -> {
                    if (!pendingExtremeConfirm) {
                        pendingExtremeConfirm = true;
                    } else {
                        module.setProfile(BehaviorProfile.EXTREME);
                        pendingExtremeConfirm = false;
                    }
                }).dimensions(cx + 60, sy, 100, 20).build());

        sy += 30;
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Start Test"),
                btn -> module.startSimulation(SentinelClient.getInstance().getLogger())).dimensions(cx - 160, sy, 75, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Stop Test"),
                btn -> module.stopSimulation(SentinelClient.getInstance().getLogger())).dimensions(cx - 80, sy, 75, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Reset Module"),
                btn -> module.resetToDefaults(SentinelClient.getInstance().getLogger())).dimensions(cx, sy, 80, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                btn -> {
                    SentinelClient.getInstance().saveConfiguration();
                    if (client != null) client.setScreen(parent);
                }).dimensions(cx + 85, sy, 75, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC0B0F17);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 18, 0xFFF8FAFC);
        context.drawCenteredTextWithShadow(this.textRenderer, module.getDescription(), this.width / 2, 32, 0xFF94A3B8);

        int infoY = 166;
        String stats = String.format("Intensity: %.0f%% | Duration: %ds | Active Profile: %s",
                module.getIntensity() * 100.0, module.getDurationSeconds(), module.getProfile().getLabel());
        context.drawCenteredTextWithShadow(this.textRenderer, stats, this.width / 2, infoY, 0xFF38BDF8);

        if (pendingExtremeConfirm || module.getProfile() == BehaviorProfile.EXTREME) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    "WARNING: Extreme testing profile generates high-intensity simulation telemetry. Use only in authorized test environments.",
                    this.width / 2, infoY + 14, 0xFFF87171);
        }

        int logY = infoY + 30;
        for (int i = 0; i < Math.min(5, module.getModuleLogs().size()); i++) {
            context.drawText(this.textRenderer, module.getModuleLogs().get(i), this.width / 2 - 160, logY + (i * 11), 0xFFCBD5E1, false);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
