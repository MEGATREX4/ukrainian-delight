package com.megatrex4.ukrainian_dlight.screen.renderer;

import com.google.common.base.Preconditions;
import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.config.ModConfig;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

// CREDIT: https://github.com/mezz/JustEnoughItems by mezz (Forge Version)
// HIGHLY EDITED VERSION FOR FABRIC by Kaupenjoe
// UPDATE for my porpose by MEGATREX4
// Under MIT-License: https://github.com/mezz/JustEnoughItems/blob/1.18/LICENSE.txt
public class FluidStackRenderer implements IIngredientRenderer<FluidStack> {
    private static final NumberFormat nf = NumberFormat.getIntegerInstance();
    public final long capacityMb;
    public final long ItemCapacityMb = ModConfig.getBrewingKegCapacity();
    private final TooltipMode tooltipMode;
    private final int width;
    private final int height;

    enum TooltipMode {
        SHOW_AMOUNT,
        SHOW_AMOUNT_AND_CAPACITY,
        ITEM_LIST
    }

    public FluidStackRenderer() {
        this(FluidStack.convertDropletsToMb(FluidConstants.BUCKET), TooltipMode.SHOW_AMOUNT_AND_CAPACITY, 16, 16);
    }

    public FluidStackRenderer(long capacityMb, boolean showCapacity, int width, int height) {
        this(capacityMb, showCapacity ? TooltipMode.SHOW_AMOUNT_AND_CAPACITY : TooltipMode.SHOW_AMOUNT, width, height);
    }

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public FluidStackRenderer(int capacityMb, boolean showCapacity, int width, int height) {
        this(capacityMb, showCapacity ? TooltipMode.SHOW_AMOUNT_AND_CAPACITY : TooltipMode.SHOW_AMOUNT, width, height);
    }

    private FluidStackRenderer(long capacityMb, TooltipMode tooltipMode, int width, int height) {
        Preconditions.checkArgument(capacityMb > 0, "capacity must be > 0");
        Preconditions.checkArgument(width > 0, "width must be > 0");
        Preconditions.checkArgument(height > 0, "height must be > 0");
        this.capacityMb = capacityMb;
        this.tooltipMode = tooltipMode;
        this.width = width;
        this.height = height;
    }

    /*
     * METHOD FROM https://github.com/TechReborn/TechReborn
     * UNDER MIT LICENSE: https://github.com/TechReborn/TechReborn/blob/1.19/LICENSE.md
     */
    public void drawFluid(DrawContext context, FluidStack fluid, int x, int y, int width, int height, long maxCapacity) {
        if (fluid.getFluidVariant().getFluid() == Fluids.EMPTY) {
            return;
        }

        // Set texture to the block atlas texture
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

        // Calculate the height of the fluid based on the current amount
        y += height;
        final Sprite sprite = FluidVariantRendering.getSprite(fluid.getFluidVariant());
        int color = FluidVariantRendering.getColor(fluid.getFluidVariant());

        final int drawHeight = (int) (fluid.getAmount() / (maxCapacity * 1F) * height);
        final int iconHeight = sprite.getContents().getHeight();
        int offsetHeight = drawHeight;

        // Set shader color to the fluid color
        RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1F);

        // Draw the fluid texture iteratively
        int iteration = 0;
        while (offsetHeight != 0) {
            final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;
            context.drawSprite(x, y - offsetHeight, 0, width, curHeight, sprite);
            offsetHeight -= curHeight;
            iteration++;
            if (iteration > 50) {
                break;
            }
        }

        // Reset shader color to white
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        // Log an error if the fluid texture is not found
        if (sprite == null) {
            UkrainianDelight.LOGGER.warn("Failed to load texture: {}", Registries.FLUID.getId(fluid.getFluidVariant().getFluid()));
        }
    }





    @Override
    public List<Text> getTooltip(FluidStack fluidStack, TooltipContext tooltipFlag) {
        List<Text> tooltip = new ArrayList<>();
        FluidVariant fluidType = fluidStack.getFluidVariant();
        if (fluidType == null) {
            return tooltip;
        }

        long amount = fluidStack.getAmount();
        if (amount > 0) {
            MutableText displayName = Text.translatable("block." + Registries.FLUID.getId(fluidStack.getFluidVariant().getFluid()).toTranslationKey());
            tooltip.add(displayName);

            if (tooltipMode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
                MutableText amountString = UkrainianDelight.i18n("tooltip.tank_amount_with_capacity", nf.format(amount), nf.format(capacityMb));
                tooltip.add(amountString.fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else if (tooltipMode == TooltipMode.SHOW_AMOUNT) {
                MutableText amountString = UkrainianDelight.i18n("tooltip.tank_amount", nf.format(amount));
                tooltip.add(amountString.fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        } else {
            tooltip.add(UkrainianDelight.i18n("tooltip.tank_empty").formatted(Formatting.DARK_GRAY));
        }

        return tooltip;
    }



    // get tooltip for items like [ %s ] - %d / %d mb
    public List<Text> getItemTooltip(FluidStack fluidStack, TooltipContext tooltipFlag) {
        List<Text> tooltip = new ArrayList<>();
        FluidVariant fluidType = fluidStack.getFluidVariant();
        if (fluidType == null) {
            return tooltip;
        }

        long amount = fluidStack.getAmount();
        if (amount > 0) {
            MutableText displayName = Text.translatable("block." + Registries.FLUID.getId(fluidStack.getFluidVariant().getFluid()).toTranslationKey());

            if (tooltipMode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
                MutableText amountString = UkrainianDelight.i18n("tooltip.item_amout_with_capacity", displayName, nf.format(amount), nf.format(ItemCapacityMb));
                tooltip.add(amountString.fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else if (tooltipMode == TooltipMode.SHOW_AMOUNT) {
                MutableText amountString = UkrainianDelight.i18n("tooltip.item_tank_amount",displayName, nf.format(amount));
                tooltip.add(amountString.fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        } else {
            tooltip.add(UkrainianDelight.i18n("tooltip.tank_empty").formatted(Formatting.DARK_GRAY));
        }

        return tooltip;
    }



    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
