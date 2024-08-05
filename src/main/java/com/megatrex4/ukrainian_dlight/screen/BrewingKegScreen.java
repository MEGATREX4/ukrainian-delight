package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.config.ModConfig;
import com.megatrex4.ukrainian_dlight.screen.renderer.FluidStackRenderer;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import com.megatrex4.ukrainian_dlight.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrewingKegScreen extends HandledScreen<BrewingKegScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(UkrainianDelight.MOD_ID, "textures/gui/brewing_keg_gui.png");


    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int WATER_SLOT = 7;
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

    private FluidStackRenderer fluidStackRenderer;


    public BrewingKegScreen(BrewingKegScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = 53;
        playerInventoryTitleY = 1000;
        assignFluidStackRenderer();
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(ModConfig.getBrewingKegCapacity(), true, 16, 40);
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

        // Use the instance to call drawFluid
        fluidStackRenderer.drawFluid(context, handler.fluidStack, x + 30, y + 12, 16, 40,
                ModConfig.getBrewingKegCapacity());
    }


    protected void renderTooltip(MatrixStack matrices, List<Text> text, int x, int y) {
        this.renderTooltip(matrices, text, x, y);
    }


    private void renderProgressBar(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 110, y + 27, 177, 17, 18, handler.getScaledProgress());
        }
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
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
            if (focusedSlot.id == BrewingKegBlockEntity.DRINKS_DISPLAY_SLOT) {
                List<Text> tooltip = new ArrayList<>();

                ItemStack drink = focusedSlot.getStack();
                Text text = drink.getName();
                if (text instanceof MutableText mutableName) {
                    tooltip.add(mutableName.formatted(drink.getRarity().formatting));
                } else {
                    tooltip.add(text);
                }
                drink.getItem().appendTooltip(drink, handler.blockEntity.getWorld(), tooltip, TooltipContext.Default.BASIC);

                ItemStack containerItem = handler.blockEntity.getDrinkContainer();
                String container = !containerItem.isEmpty() ? containerItem.getItem().getName().getString() : "";

                tooltip.add(UkrainianDelight.i18n("tooltip.poured", container).formatted(Formatting.GRAY));

                context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
            } else {
                context.drawItemTooltip(textRenderer, focusedSlot.getStack(), mouseX, mouseY);
            }
        }
    }







    protected void drawMouseoverTankTooltip(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        if (isMouseAboveArea(mouseX, mouseY, x, y, 30, 12, fluidStackRenderer)) {
            List<Text> tooltip = fluidStackRenderer.getTooltip(handler.fluidStack, TooltipContext.Default.BASIC);
            context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
        }
    }


    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }



}
