package com.sentinel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class SentinelConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "sentinel-client.json";

    public String activeProfile = "default";
    public GuiSettings gui = new GuiSettings();
    public Map<String, ModuleConfigData> modules = new LinkedHashMap<>();

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }

    public static SentinelConfig load() {
        Path path = getConfigPath();
        if (!Files.exists(path)) {
            SentinelConfig cfg = new SentinelConfig();
            cfg.save();
            return cfg;
        }
        try {
            String raw = Files.readString(path, StandardCharsets.UTF_8);
            SentinelConfig loaded = GSON.fromJson(raw, SentinelConfig.class);
            if (loaded == null) return new SentinelConfig();
            if (loaded.gui == null) loaded.gui = new GuiSettings();
            if (loaded.modules == null) loaded.modules = new LinkedHashMap<>();
            return loaded;
        } catch (IOException | RuntimeException e) {
            return new SentinelConfig();
        }
    }

    public void save() {
        Path path = getConfigPath();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(this), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save Sentinel configuration to " + path, e);
        }
    }
}
