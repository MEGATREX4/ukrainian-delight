package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class JarsItems {
    public static final Item JAR = registerJarsItem("jar", ModBlocks.JAR_BLOCK.getDefaultState());
    public static final Item APPLE_JAM = registerJarsItem("apple_jam", ModBlocks.APPLE_JAM_BLOCK.getDefaultState());
    public static final Item TINNED_TOMATOES = registerJarsItem("tinned_tomatoes", ModBlocks.TINNED_TOMATOES_BLOCK.getDefaultState());

    private static Item registerJarsItem(String name, BlockState blockState) {
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name), new JarBuilder(blockState, new Item.Settings()));
    }

    public static void registerJarsItems() {
        // Register items here if necessary
    }
}
