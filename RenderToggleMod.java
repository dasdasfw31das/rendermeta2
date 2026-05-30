package com.rendertoggle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class RenderToggleMod implements ClientModInitializer {

    private static KeyBinding toggleRenderKey;
    private static final int MIN_RENDER_DISTANCE = 2;
    private static final int MAX_RENDER_DISTANCE = 32;

    @Override
    public void onInitializeClient() {
        toggleRenderKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rendertoggle.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.rendertoggle.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleRenderKey.wasPressed()) {
                toggleRenderDistance(client);
            }
        });
    }

    private void toggleRenderDistance(MinecraftClient client) {
        if (client.options == null) return;
        int current = client.options.getViewDistance().getValue();
        if (current <= MIN_RENDER_DISTANCE) {
            client.options.getViewDistance().setValue(MAX_RENDER_DISTANCE);
            client.options.write();
            if (client.player != null)
                client.player.sendMessage(Text.literal("§aRender distance: §f" + MAX_RENDER_DISTANCE + " §7(max)"), true);
        } else {
            client.options.getViewDistance().setValue(MIN_RENDER_DISTANCE);
            client.options.write();
            if (client.player != null)
                client.player.sendMessage(Text.literal("§cRender distance: §f" + MIN_RENDER_DISTANCE + " §7(min)"), true);
        }
    }
}
