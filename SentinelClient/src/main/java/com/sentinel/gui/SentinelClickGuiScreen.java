package com.sentinel.gui;

import com.sentinel.SentinelClient;
import com.sentinel.config.GuiSettings;
import com.sentinel.gui.components.ModuleCardWidget;
import com.sentinel.modules.Category;
import com.sentinel.modules.SentinelModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SentinelClickGuiScreen extends Screen {
    private Category selectedCategory = Category.COMBAT;
    private TextFieldWidget searchField;
    private int scrollOffset = 0;
    private final List<ModuleCardWidget> visibleCards = new ArrayList<>();

    public SentinelClickGuiScreen() {
        super(Text.literal("Sentinel Client GUI"));
    }

    @Override
    protected void init() {
        visibleCards.clear();
        int leftNavX = 14;
        int navY = 44;

        for (Category cat : Category.values()) {
            addDrawableChild(ButtonWidget.builder(Text.literal(cat.getDisplayName()), btn -> {
                this.selectedCategory = cat;
                this.scrollOffset = 0;
            }).dimensions(leftNavX, navY, 108, 18).build());
            navY += 21;
        }

        searchField = new TextFieldWidget(this.textRenderer, 134, 16, 180, 18, Text.literal("Search modules..."));
        searchField.setPlaceholder(Text.literal("Search modules..."));
        addDrawableChild(searchField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Dashboard"), btn -> {
            if (client != null) client.setScreen(new SentinelDashboardScreen(this));
        }).dimensions(this.width - 188, 15, 84, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Save Config"), btn -> {
            SentinelClient.getInstance().saveConfiguration();
        }).dimensions(this.width - 98, 15, 84, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        GuiSettings guiCfg = SentinelClient.getInstance().getConfig().gui;
        int accent = "Emerald Night".equals(guiCfg.getTheme()) ? 0xFF10B981
                : ("Crimson Ops".equals(guiCfg.getTheme()) ? 0xFFEF4444 : 0xFF38BDF8);

        context.fill(0, 0, this.width, this.height, 0xCC070A12);

        // Header bar
        context.fill(10, 10, this.width - 10, 38, 0xFF111827);
        context.drawBorder(10, 10, this.width - 20, 28, 0xFF1F2937);
        context.drawTextWithShadow(this.textRenderer, "SENTINEL CLIENT", 20, 19, accent);

        // Left category rail
        context.fill(10, 42, 126, this.height - 12, 0xFF111827);
        context.drawBorder(10, 42, 116, this.height - 54, 0xFF1F2937);

        // Main module panel
        int panelX = 132;
        int panelY = 42;
        int panelW = this.width - panelX - 10;
        int panelH = this.height - panelY - 12;

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xFF0F172A);
        context.drawBorder(panelX, panelY, panelW, panelH, 0xFF1E293B);

        visibleCards.clear();
        if (selectedCategory == Category.SETTINGS) {
            renderClientSettingsPanel(context, panelX + 12, panelY + 12, panelW - 24, guiCfg);
        } else {
            String query = searchField != null ? searchField.getText().trim().toLowerCase() : "";
            List<SentinelModule> list = SentinelClient.getInstance().getModuleManager().getByCategory(selectedCategory);
            int cardY = panelY + 10 - scrollOffset;
            int cardW = panelW - 20;
            int cardH = 48;

            for (SentinelModule m : list) {
                if (!query.isEmpty() && !m.getName().toLowerCase().contains(query)
                        && !m.getDescription().toLowerCase().contains(query)) {
                    continue;
                }
                if (cardY + cardH >= panelY + 6 && cardY <= panelY + panelH - 8) {
                    ModuleCardWidget card = new ModuleCardWidget(m, panelX + 10, cardY, cardW, cardH);
                    card.render(context, this.textRenderer, mouseX, mouseY, accent);
                    visibleCards.add(card);
                }
                cardY += cardH + 6;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderClientSettingsPanel(DrawContext context, int x, int y, int w, GuiSettings cfg) {
        context.drawTextWithShadow(this.textRenderer, "Client & GUI Settings", x, y, 0xFFF8FAFC);
        context.drawText(this.textRenderer, "GUI Hotkey: 0 (GLFW 48) | Theme: " + cfg.getTheme() + " | Scale: " + String.format("%.2f", cfg.getScale()),
                x, y + 16, 0xFF94A3B8, false);
        context.drawText(this.textRenderer, "Config File: config/sentinel-client.json", x, y + 30, 0xFF64748B, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ModuleCardWidget card : visibleCards) {
            ModuleCardWidget.Action act = card.click(mouseX, mouseY);
            if (act == ModuleCardWidget.Action.TOGGLE_MODULE) {
                card.getModule().toggle(SentinelClient.getInstance().getLogger());
                return true;
            } else if (act == ModuleCardWidget.Action.OPEN_SETTINGS) {
                if (client != null) {
                    client.setScreen(new ModuleSettingsScreen(card.getModule(), this));
                }
                return true;
            } else if (act == ModuleCardWidget.Action.RESET_MODULE) {
                card.getModule().resetToDefaults(SentinelClient.getInstance().getLogger());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 18));
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
