package com.sentinel.gui.components;

import com.sentinel.modules.SentinelModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

public class ModuleCardWidget {
    private final SentinelModule module;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean hovered;

    public ModuleCardWidget(SentinelModule module, int x, int y, int width, int height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY, int accentColor) {
        this.hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        int bgColor = module.isEnabled() ? 0xFF1E293B : (hovered ? 0xFF1A1F2C : 0xFF131722);
        int borderColor = module.isEnabled() ? accentColor : (hovered ? 0xFF334155 : 0xFF1E293B);

        context.fill(x, y, x + width, y + height, bgColor);
        context.drawBorder(x, y, width, height, borderColor);

        String statusBadge = module.isEnabled() ? "[ON]" : "[OFF]";
        int badgeColor = module.isEnabled() ? 0xFF4ADE80 : 0xFF94A3B8;
        context.drawTextWithShadow(textRenderer, module.getName(), x + 8, y + 6, 0xFFF8FAFC);
        context.drawTextWithShadow(textRenderer, statusBadge, x + width - textRenderer.getWidth(statusBadge) - 8, y + 6, badgeColor);

        String shortDesc = textRenderer.trimToWidth(module.getDescription(), width - 16);
        context.drawText(textRenderer, shortDesc, x + 8, y + 19, 0xFF94A3B8, false);

        // Settings & Reset action chips
        context.fill(x + 8, y + height - 18, x + 68, y + height - 5, 0xFF273043);
        context.drawText(textRenderer, "Settings", x + 14, y + height - 15, 0xFFE2E8F0, false);

        context.fill(x + 74, y + height - 18, x + 116, y + height - 5, 0xFF3B1D24);
        context.drawText(textRenderer, "Reset", x + 81, y + height - 15, 0xFFFCA5A5, false);
    }

    public Action click(double mouseX, double mouseY) {
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
            return Action.NONE;
        }
        if (mouseY >= y + height - 18 && mouseY <= y + height - 5) {
            if (mouseX >= x + 8 && mouseX <= x + 68) return Action.OPEN_SETTINGS;
            if (mouseX >= x + 74 && mouseX <= x + 116) return Action.RESET_MODULE;
        }
        return Action.TOGGLE_MODULE;
    }

    public SentinelModule getModule() { return module; }
    public int getY() { return y; }
    public int getHeight() { return height; }

    public enum Action {
        NONE, TOGGLE_MODULE, OPEN_SETTINGS, RESET_MODULE
    }
}
