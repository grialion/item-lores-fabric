package tech.grial.itemlores.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.visitor.StringNbtWriter;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.awt.*;

public class LoreGuiScreen extends Screen {

    public LoreGuiScreen() {
        super(Text.of("Item Lore"));
    }

    // TODO: better validation
    private boolean validLore(@Nullable String lore) {
        return lore == null || lore.startsWith("['") && lore.endsWith("']") && lore.length() > 4 || "Example Lore".equals(lore);
    }

    private String getLore() {
        if (client != null && client.player != null) {
            ItemStack activeItem = client.player.getMainHandStack();
            if (activeItem != null && activeItem.hasNbt()) {
                NbtCompound nbt = activeItem.getNbt();
                if (nbt != null && nbt.contains("display")) {
                    NbtCompound display = nbt.getCompound("display");
                    if (display.contains("Lore")) {
                        return (new StringNbtWriter()).apply(display.get("Lore"));
                    }
                } else return null;
            } else if (activeItem != null && !(activeItem.getItem() instanceof AirBlockItem)) {
                return null;
            }
        }

        return "Example Lore";
    }

    private void copyToClipBoard() {
        String lore = getLore();
        if (lore != null && client != null && !"Example Lore".equals(lore))
            client.keyboard.setClipboard(lore);
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(width / 2 - 52, 30, 105, 20, Text.translatable("chat.copy"), (button) -> this.copyToClipBoard()));

        // TODO: edit field
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrixStack);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String lore = getLore();
        String loreString = "Lore: " + getLore();
        if (lore == null) {
            loreString = "No lore found!";
        }
        textRenderer.draw(matrixStack, Text.of(loreString), width / 2F - textRenderer.getWidth(loreString) / 2F, 10, validLore(lore) ? Color.white.getRGB() : Color.red.getRGB());

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
