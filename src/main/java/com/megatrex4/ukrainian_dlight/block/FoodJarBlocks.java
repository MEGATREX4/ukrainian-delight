package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.custom.JarBlock;
import com.megatrex4.ukrainian_dlight.item.FoodBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FoodJarBlocks {

    // New Jar Blocks
    public static final Block JAR = registerBlock("jar", new JarBlock(), null);

    //JAMS
    public static final Block APPLE_JAM = registerBlock("apple_jam", new JarBlock(), new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.3f)
            .build());

    //JARRED VEGETABLES
    public static final Block JARRED_TOMATOES = registerBlock("jarred_tomatoes", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            //.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 3 * 20, 3), 1.0f)
            .build());

    public static final Block JARRED_CABBAGE = registerBlock("jarred_cabbage", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .build());

    public static final Block JARRED_BEETROOT = registerBlock("jarred_beetroot", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .build());

    public static final Block JARRED_CARROT = registerBlock("jarred_carrot", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .build());

    public static final Block JARRED_ONION = registerBlock("jarred_onion", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .build());

    public static final Block JARRED_BORSCHT = registerBlock("jarred_borscht", new JarBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .build());


    private static Block registerBlock(String name, Block block, FoodComponent foodComponent) {
        registerBlockItem(name, block, foodComponent);
        return Registry.register(Registries.BLOCK, new Identifier(UkrainianDelight.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, FoodComponent foodComponent) {
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
