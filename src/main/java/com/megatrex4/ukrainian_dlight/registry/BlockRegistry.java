package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.BrewingKegBlock;
import com.megatrex4.ukrainian_dlight.block.BrewingKegBlockItem;
import com.megatrex4.ukrainian_dlight.block.CucumberCropBlock;
import com.megatrex4.ukrainian_dlight.block.SaltBlock;
import com.megatrex4.ukrainian_dlight.util.UDIdentifier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    public static final Block SALT_BLOCK = registerBlock("salt_block",
            new SaltBlock(Block.Settings.copy(Blocks.STONE).sounds(BlockSoundGroup.STONE)));

    public static final Block SALT_BAG = registerBlock("salt_bag",
    new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL)));

    public static final Block CUCUMBER_CRATE = registerBlock("cucumber_crate",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD)));

    public static final Block BREWING_KEG = registerBlock("brewing_keg",
            new BrewingKegBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD).nonOpaque()));

    public static final Block CUCUMBER_CROP = registerBlock("cucumber_crop", new CucumberCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT)));

    //simple blocks
    public static final Block POPLAR_PLANKS = registerBlock("poplar_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
    public static final Block POPLAR_LEAVES = registerBlock("poplar_leaves", new Block(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)));
    public static final Block POPLAR_LOG = registerBlock("poplar_log", new Block(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new UDIdentifier(name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Item item;
        if (block instanceof BrewingKegBlock) {
            item = new BrewingKegBlockItem(block, new Item.Settings().maxCount(1));
        } else {
            item = new BlockItem(block, new Item.Settings());
        }
        Registry.register(Registries.ITEM, new UDIdentifier(name), item);
    }

    public static void registerModBlocks() {
        UkrainianDelight.LOGGER.info("Registering Mod Blocks for " + UkrainianDelight.MOD_ID);
    }


}
