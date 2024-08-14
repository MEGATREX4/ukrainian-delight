package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.block.FoodJarBlocks;
import com.megatrex4.ukrainian_dlight.registry.BlockRegistry;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {
    public static final ItemGroup MEALS_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "meals_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.meals_ukrainian_delight"))
                    .icon(() -> new ItemStack(ItemsRegistry.BORSCHT))
                    .entries((displayContext, entries) -> {
                        entries.add(ItemsRegistry.BORSCHT);
                        entries.add(ItemsRegistry.LEAN_BORSCHT);
                        entries.add(ItemsRegistry.HOMEMADE_SAUSAGE);
                        entries.add(ItemsRegistry.VARENYK);
                    }).build());

    public static final ItemGroup INGREDIENT_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "ingredients_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.ingredients_ukrainian_delight"))
                    .icon(() -> new ItemStack(ItemsRegistry.CUCUMBER))
                    .entries((displayContext, entries) -> {
                        entries.add(BlockRegistry.BREWING_KEG);
                        entries.add(BlockRegistry.SALT_BAG);
                        entries.add(BlockRegistry.SALT_BLOCK);
                        entries.add(ItemsRegistry.SALT);

                        entries.add(ItemsRegistry.HORSERADISH);
                        entries.add(BlockRegistry.CUCUMBER_CRATE);
                        entries.add(ItemsRegistry.CUCUMBER);
                        entries.add(ItemsRegistry.CUTTED_CUCUMBER);
                        entries.add(ItemsRegistry.APPLE_SLICE);
                        entries.add(ItemsRegistry.DRIED_APPLE_SLICE);
                        entries.add(ItemsRegistry.CHERRY_BERRY);
                        entries.add(ItemsRegistry.COTTAGE_CHEESE);
                        entries.add(ItemsRegistry.SALO);
                        entries.add(ItemsRegistry.YEAST);
                    }).build());
    
    public static final ItemGroup KRASHANKY_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "krashanky_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.krashanky_ukrainian_delight"))
                    .icon(() -> new ItemStack(ItemsRegistry.BLUE_KRASHANKA))
                    .entries((displayContext, entries) -> {
                        entries.add(ItemsRegistry.BLACK_KRASHANKA);
                        entries.add(ItemsRegistry.BLUE_KRASHANKA);
                        entries.add(ItemsRegistry.BROWN_KRASHANKA);
                        entries.add(ItemsRegistry.CYAN_KRASHANKA);
                        entries.add(ItemsRegistry.GRAY_KRASHANKA);
                        entries.add(ItemsRegistry.GREEN_KRASHANKA);
                        entries.add(ItemsRegistry.LIGHT_BLUE_KRASHANKA);
                        entries.add(ItemsRegistry.LIGHT_GRAY_KRASHANKA);
                        entries.add(ItemsRegistry.LIME_KRASHANKA);
                        entries.add(ItemsRegistry.MAGENTA_KRASHANKA);
                        entries.add(ItemsRegistry.ORANGE_KRASHANKA);
                        entries.add(ItemsRegistry.PINK_KRASHANKA);
                        entries.add(ItemsRegistry.PURPLE_KRASHANKA);
                        entries.add(ItemsRegistry.RED_KRASHANKA);
                        entries.add(ItemsRegistry.WHITE_KRASHANKA);
                        entries.add(ItemsRegistry.YELLOW_KRASHANKA);


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
