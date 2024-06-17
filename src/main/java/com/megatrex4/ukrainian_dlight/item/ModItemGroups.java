package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.FoodBlocks;
import com.megatrex4.ukrainian_dlight.block.ModBlock;
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
                        entries.add(ModBlock.SALT_BLOCK);
                        entries.add(ModItems.SALT);

                        entries.add(ModItems.HORSERADISH);
                        entries.add(ModBlock.CUCUMBER_CRATE);
                        entries.add(ModItems.CUCUMBER);
                        entries.add(ModItems.CUTTED_CUCUMBER);
                        entries.add(ModItems.APPLE_SLICE);
                        entries.add(ModItems.DRIED_APPLE_SLICE);
                    }).build());

    public static final ItemGroup JARS_UKRAINIAN_DELIGHT = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "jars_ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.jars_ukrainian_delight"))
                    .icon(() -> new ItemStack(FoodBlocks.JARRED_TOMATOES))
                    .entries((displayContext, entries) -> {
                        entries.add(FoodBlocks.JAR);
                        entries.add(FoodBlocks.APPLE_JAM);
                        entries.add(FoodBlocks.JARRED_TOMATOES);
                        entries.add(FoodBlocks.JARRED_CABBAGE);
                        entries.add(FoodBlocks.JARRED_BEETROOT);
                    }).build());

    public static void registerItemGroups() {
        UkrainianDelight.LOGGER.info("Registering Item Groups for " + UkrainianDelight.MOD_ID);
    }


}
