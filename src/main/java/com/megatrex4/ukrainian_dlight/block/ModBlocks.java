package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SALT_BLOCK = registerBlock("salt_block",
            new Block(FabricBlockSettings.copyOf(Blocks.SAND).sounds(BlockSoundGroup.WET_GRASS)));
    public static final Block CUCUMBER_CRATE = registerBlock("cucumber_crate",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD)));

    // New Jar Blocks
    public static final Block JAR = registerBlock("jar", new JarBlock());
    public static final Block APPLE_JAM = registerBlock("apple_jam", new JarBlock());
    public static final Block TINNED_TOMATOES = registerBlock("canned_tomatoes", new JarBlock());

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(UkrainianDelight.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        UkrainianDelight.LOGGER.info("Registering Mod Blocks for " + UkrainianDelight.MOD_ID);
    }
}
