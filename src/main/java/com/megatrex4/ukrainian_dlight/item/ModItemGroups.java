package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.block.FoodJarBlocks;
import com.megatrex4.ukrainian_dlight.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup MEALS_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "meals_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.meals_ukrainian_delight"))
                    .icon(() -> new ItemStack(ModItems.BORSCHT))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BORSCHT);
                        entries.add(ModItems.LEAN_BORSCHT);
                        entries.add(ModItems.HOMEMADE_SAUSAGE);
                        entries.add(ModItems.VARENYK);
                    }).build());

    public static final ItemGroup INGREDIENT_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "ingredients_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.ingredients_ukrainian_delight"))
                    .icon(() -> new ItemStack(ModItems.CUCUMBER))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.BREWING_KEG);
                        entries.add(ModBlocks.SALT_BAG);
                        entries.add(ModBlocks.SALT_BLOCK);
                        entries.add(ModItems.SALT);

                        entries.add(ModItems.HORSERADISH);
                        entries.add(ModBlocks.CUCUMBER_CRATE);
                        entries.add(ModItems.CUCUMBER);
                        entries.add(ModItems.CUTTED_CUCUMBER);
                        entries.add(ModItems.APPLE_SLICE);
                        entries.add(ModItems.DRIED_APPLE_SLICE);
                        entries.add(ModItems.CHERRY_BERRY);
                        entries.add(ModItems.COTTAGE_CHEESE);
                        entries.add(ModItems.SALO);
                        entries.add(ModItems.YEAST);
                    }).build());
    
    public static final ItemGroup KRASHANKY_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "krashanky_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.krashanky_ukrainian_delight"))
                    .icon(() -> new ItemStack(ModItems.APPLE_SLICE))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BLACK_KRASHANKA);
                        entries.add(ModItems.BLUE_KRASHANKA);
                        entries.add(ModItems.BROWN_KRASHANKA);
                        entries.add(ModItems.CYAN_KRASHANKA);
                        entries.add(ModItems.GRAY_KRASHANKA);
                        entries.add(ModItems.GREEN_KRASHANKA);
                        entries.add(ModItems.LIGHT_BLUE_KRASHANKA);
                        entries.add(ModItems.LIGHT_GRAY_KRASHANKA);
                        entries.add(ModItems.LIME_KRASHANKA);
                        entries.add(ModItems.MAGENTA_KRASHANKA);
                        entries.add(ModItems.ORANGE_KRASHANKA);
                        entries.add(ModItems.PINK_KRASHANKA);
                        entries.add(ModItems.PURPLE_KRASHANKA);
                        entries.add(ModItems.RED_KRASHANKA);
                        entries.add(ModItems.WHITE_KRASHANKA);
                        entries.add(ModItems.YELLOW_KRASHANKA);

                    }).build());

    public static final ItemGroup JARS_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "jars_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.jars_ukrainian_delight"))
                    .icon(() -> new ItemStack(FoodJarBlocks.JARRED_TOMATOES))
                    .entries((displayContext, entries) -> {
                        entries.add(FoodJarBlocks.JAR);
                        entries.add(FoodJarBlocks.APPLE_JAM);
                        entries.add(FoodJarBlocks.JARRED_TOMATOES);
                        entries.add(FoodJarBlocks.JARRED_CABBAGE);
                        entries.add(FoodJarBlocks.JARRED_BEETROOT);
                        entries.add(FoodJarBlocks.JARRED_CARROT);
                        entries.add(FoodJarBlocks.JARRED_ONION);
                        entries.add(FoodJarBlocks.JARRED_BORSCHT);

                        entries.add(DrinkBottleBlock.WINE_BOTTLE);
                    }).build());

    public static void registerItemGroups() {
        UkrainianDelight.LOGGER.info("Registering Item Groups for " + UkrainianDelight.MOD_ID);
    }


}
