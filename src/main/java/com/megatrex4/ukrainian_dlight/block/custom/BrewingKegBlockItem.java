package com.megatrex4.ukrainian_dlight.block.custom;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

public class BrewingKegBlockItem extends BlockItem {

    public BrewingKegBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getDrinkCount(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int drinkCount = getDrinkCount(stack);
        int maxCapacity = getMaxCapacity(stack); // Get max capacity from item data
        return Math.round(13.0F * drinkCount / (float) maxCapacity);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        // Convert HEX #6666ff to HSV
        float hue = 240.0F / 360.0F; // Hue for #6666ff is approximately 240 degrees
        float saturation = 0.6F; // Saturation of #6666ff
        float value = 1.0F; // Full brightness

        return MathHelper.hsvToRgb(hue, saturation, value);
    }





    private int getDrinkCount(ItemStack stack) {
        if (stack.getNbt() != null && stack.getNbt().contains("BlockEntityTag")) {
            NbtCompound tag = stack.getNbt().getCompound("BlockEntityTag");
            if (tag.contains("DisplaySlot", NbtElement.COMPOUND_TYPE)) {
                ItemStack drink = ItemStack.fromNbt(tag.getCompound("DisplaySlot"));
                return drink.getCount();
            }
        }
        return 0;
    }

    public static int getMaxCapacity(ItemStack stack) {
        if (stack.getNbt() != null && stack.getNbt().contains("BlockEntityTag")) {
            NbtCompound tag = stack.getNbt().getCompound("BlockEntityTag");
            if (tag.contains("DisplaySlot", NbtElement.COMPOUND_TYPE)) {
                ItemStack drink = ItemStack.fromNbt(tag.getCompound("DisplaySlot"));
                // Return the max stack size of the item in DisplaySlot
                return drink.getMaxCount();
            }
        }        // Default capacity if no valid DisplaySlot item is found
        return 0;
    }

}
