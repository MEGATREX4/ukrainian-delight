package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BrewingKegScreen extends HandledScreen<BrewingKegScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(UkrainianDelight.MOD_ID, "textures/gui/brewing_keg_gui.png");


    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int REQUIRE_CONTAINER = 7;
    public static final int WATER_SLOT = 8;
    public static final int DRINKS_DISPLAY_SLOT = 9;
    public static final int OUTPUT_SLOT = 10;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;


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
        drawMouseoverTankTooltip(context, mouseX, mouseY);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int mouseX, int mouseY) {
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null) {
            if (this.focusedSlot.hasStack()) {
                // Check if the focused slot is DRINKS_DISPLAY_SLOT (slot ID 9)
                if (this.focusedSlot.id == DRINKS_DISPLAY_SLOT) {
                    List<Text> tooltip = new ArrayList<>();

                    // Get the item in DRINKS_DISPLAY_SLOT (slot ID 9)
                    ItemStack drink = this.focusedSlot.getStack();
                    Text drinkText = drink.getName();
                    if (drinkText instanceof MutableText mutableName) {
                        tooltip.add(mutableName.formatted(drink.getRarity().formatting));
                    } else {
                        tooltip.add(drinkText);
                    }
                    drink.getItem().appendTooltip(drink, handler.blockEntity.getWorld(), tooltip, TooltipContext.Default.BASIC);

                    // Get the item in REQUIRE_CONTAINER (slot ID 7)
                    ItemStack containerItem = handler.blockEntity.getStack(REQUIRE_CONTAINER);
                    String containerName = "";
                    if (!containerItem.isEmpty()) {
                        Item container = containerItem.getItem();
                        containerName = Text.translatable(container.getTranslationKey()).getString();
                    }

                    // Add the localized string with the container name
                    tooltip.add(UkrainianDelight.i18n("tooltip.slot_item", containerName).formatted(Formatting.GRAY));

                    // Draw the tooltip
                    context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
                } else {
                    // Draw the typical tooltip for all other slots
                    context.drawItemTooltip(textRenderer, this.focusedSlot.getStack(), mouseX, mouseY);
                }
            }
        }
    }






    protected void drawMouseoverTankTooltip(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        if (isMouseOverTankArea(mouseX, mouseY, x, y)) {
            List<Text> tooltip = getTankTooltip();
            context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
        }
    }

    private boolean isMouseOverTankArea(int mouseX, int mouseY, int x, int y) {
        int tankX = x + 30;
        int tankY = y + 12;
        int tankWidth = 16;
        int tankHeight = 40;

        return mouseX >= tankX && mouseX < tankX + tankWidth && mouseY >= tankY && mouseY < tankY + tankHeight;
    }

    private List<Text> getTankTooltip() {
        long fluidAmount = handler.fluidStack.amount;
        long maxFluidAmount = handler.getCapacity(); // Ensure this method exists in your handler
        MutableText fluidName = Text.translatable("block." + Registries.FLUID.getId(handler.fluidStack.fluidVariant.getFluid()).toTranslationKey());

        if (fluidAmount > 0) {
            return List.of(
                    fluidName,
                    UkrainianDelight.i18n("tooltip.water_amount", fluidAmount, maxFluidAmount).formatted(Formatting.GRAY)
            );
        } else {
            return List.of(UkrainianDelight.i18n("tooltip.tank_empty"));
        }
    }
}
