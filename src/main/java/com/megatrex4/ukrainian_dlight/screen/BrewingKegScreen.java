package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BrewingKegScreen extends HandledScreen<BrewingKegScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(UkrainianDelight.MOD_ID, "textures/gui/brewing_keg_gui.png");

    public BrewingKegScreen(BrewingKegScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = 53;
        playerInventoryTitleY = 1000;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressBar(context, x, y);
        renderWaterLevel(context, x, y);
    }

    private void renderProgressBar(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 110, y + 27, 177, 17, 18, handler.getScaledProgress());
        }
    }

    private void renderWaterLevel(DrawContext context, int x, int y) {
        int waterLevelHeight = handler.getScaledWaterLevel();
        int maxWaterLevelHeight = 40; // Maximum height of the water level indicator
        int waterBarY = 12 + (maxWaterLevelHeight - waterLevelHeight); // Adjust Y position to start from the bottom

        context.drawTexture(TEXTURE, x + 30, y + waterBarY, 178, 35, 17, waterLevelHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
