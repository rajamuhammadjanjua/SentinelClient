package com.sentinel.events;

import com.sentinel.SentinelClient;
import com.sentinel.gui.SentinelClickGuiScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static KeyBinding openGuiKey;

    public static void register() {
        KeyBinding.Category sentinelCategory = KeyBinding.Category.create(Identifier.of("sentinel-client", "main"));
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sentinel-client.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                sentinelCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(KeybindHandler::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        if (client == null || client.player == null) return;
        // Avoid triggering while typing in chat or another open screen
        if (client.currentScreen != null) {
            while (openGuiKey.wasPressed()) {
                // Consume press so it doesn't queue up while in chat/screens
            }
            return;
        }

        while (openGuiKey.wasPressed()) {
            client.setScreen(new SentinelClickGuiScreen());
        }
    }
}
