package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.custom.BrewingKegBlock;
import com.megatrex4.ukrainian_dlight.block.custom.BrewingKegBlockItem;
import com.megatrex4.ukrainian_dlight.block.custom.SaltBlock;
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
            new SaltBlock(Block.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE)));

    public static final Block SALT_BAG = registerBlock("salt_bag",
    new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL)));

    public static final Block CUCUMBER_CRATE = registerBlock("cucumber_crate",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD)));

    public static final Block BREWING_KEG = registerBlock("brewing_keg",
            new BrewingKegBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD).nonOpaque()));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(UkrainianDelight.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Item item;
        if (block instanceof BrewingKegBlock) {
            // Use custom BrewingKegBlockItem for BrewingKegBlock
            item = new BrewingKegBlockItem(block, new Item.Settings().maxCount(1));
        } else {
            // Default BlockItem for other blocks
            item = new BlockItem(block, new Item.Settings());
        }
        Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name), item);
    }

    public static void registerModBlocks() {
        UkrainianDelight.LOGGER.info("Registering Mod Blocks for " + UkrainianDelight.MOD_ID);
    }


}
