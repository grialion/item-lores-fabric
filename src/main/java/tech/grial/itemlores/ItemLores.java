package tech.grial.itemlores;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.grial.itemlores.gui.LoreGuiScreen;

public class ItemLores implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("item-lores");
    public static KeyBinding loreGuiKeyBind;

    @Override
    public void onInitialize() {
        LOGGER.info("Setting up key binds.");

        loreGuiKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.itemlores.lore_gui", InputUtil.Type.KEYSYM, -1, "category.itemlores"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (loreGuiKeyBind.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(new LoreGuiScreen());
                }
            }
        });
    }
}
