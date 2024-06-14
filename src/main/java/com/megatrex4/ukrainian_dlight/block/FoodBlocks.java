package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.item.FoodBlockComponents;
import com.megatrex4.ukrainian_dlight.item.FoodBlockItem; // Import the FoodBlockItem class
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FoodBlocks {

    // New Jar Blocks
    public static final Block JAR = registerBlock("jar", new JarBlock(), null);
    public static final Block APPLE_JAM = registerBlock("apple_jam", new JarBlock(), FoodBlockComponents.APPLE_JAM);
    public static final Block CANNED_TOMATOES = registerBlock("canned_tomatoes", new JarBlock(), FoodBlockComponents.CANNED_TOMATOES);

    private static Block registerBlock(String name, Block block, FoodComponent foodComponent){
        registerBlockItem(name, block, foodComponent);
        return Registry.register(Registries.BLOCK, new Identifier(UkrainianDelight.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, FoodComponent foodComponent){
        Item item;
        if (foodComponent != null) {
            item = new FoodBlockItem(block, foodComponent);
        } else {
            item = new BlockItem(block, new Item.Settings());
        }
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name), item);
    }

    public static void registerFoodBlocks() {
        UkrainianDelight.LOGGER.info("Registering Food Blocks for " + UkrainianDelight.MOD_ID);
    }
}
