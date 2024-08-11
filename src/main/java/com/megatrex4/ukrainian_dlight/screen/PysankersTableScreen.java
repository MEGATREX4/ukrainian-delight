package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.registry.PatternRegistry;
import com.megatrex4.ukrainian_dlight.registry.manager.PatternManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PysankersTableScreen extends HandledScreen<PysankersTableScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("ukrainian_delight", "textures/gui/container/pysankers_table.png");

    public PysankersTableScreen(PysankersTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Draw the main GUI texture
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // Get the slots from the handler
        Slot krashankaSlot = this.handler.getSlot(0);
        Slot dyeSlot = this.handler.getSlot(1);
        Slot patternSlot = this.handler.getSlot(2);
        Slot outputSlot = this.handler.getSlot(3);

        // Draw slot backgrounds
        if (!krashankaSlot.hasStack()) {
            context.drawTexture(TEXTURE, this.x + krashankaSlot.x, this.y + krashankaSlot.y, this.backgroundWidth, 0, 16, 16);
        }
        if (!dyeSlot.hasStack()) {
            context.drawTexture(TEXTURE, this.x + dyeSlot.x, this.y + dyeSlot.y, this.backgroundWidth + 16, 0, 16, 16);
        }
        if (!patternSlot.hasStack()) {
            context.drawTexture(TEXTURE, this.x + patternSlot.x, this.y + patternSlot.y, this.backgroundWidth + 32, 0, 16, 16);
        }

        // Draw the pattern if available
        ItemStack patternStack = patternSlot.getStack();
        if (!patternStack.isEmpty()) {
            // Get the pattern ID
            PatternRegistry patternRegistry = PatternManager.getPatternForItem(patternStack);
            if (patternRegistry != null) {
                Identifier patternTexture = new Identifier("ukrainian_dlight", "textures/item/krashanky/patterns/" + patternRegistry.getId().getPath() + ".png");

                // Render the pattern texture
                context.drawTexture(patternTexture, this.x + patternSlot.x + 1, this.y + patternSlot.y + 1, 0, 0, 16, 16);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}


