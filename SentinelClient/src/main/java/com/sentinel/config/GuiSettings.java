package com.sentinel.config;

public class GuiSettings {
    private double scale = 1.0;
    private int panelOffsetX = 20;
    private int panelOffsetY = 20;
    private String theme = "Obsidian Dark";
    private int guiKeyCode = 48; // GLFW_KEY_0
    private boolean extremeWarningAcknowledged = false;

    public double getScale() { return scale; }
    public void setScale(double scale) { this.scale = Math.max(0.65, Math.min(1.5, scale)); }

    public int getPanelOffsetX() { return panelOffsetX; }
    public void setPanelOffsetX(int panelOffsetX) { this.panelOffsetX = panelOffsetX; }

    public int getPanelOffsetY() { return panelOffsetY; }
    public void setPanelOffsetY(int panelOffsetY) { this.panelOffsetY = panelOffsetY; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public int getGuiKeyCode() { return guiKeyCode; }
    public void setGuiKeyCode(int guiKeyCode) { this.guiKeyCode = guiKeyCode; }

    public boolean isExtremeWarningAcknowledged() { return extremeWarningAcknowledged; }
    public void setExtremeWarningAcknowledged(boolean extremeWarningAcknowledged) {
        this.extremeWarningAcknowledged = extremeWarningAcknowledged;
    }

    public void resetDefaults() {
        this.scale = 1.0;
        this.panelOffsetX = 20;
        this.panelOffsetY = 20;
        this.theme = "Obsidian Dark";
        this.guiKeyCode = 48;
    }
}
