package com.megatrex4.ukrainian_dlight.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<BrewingKegBlockEntity> BREWING_KEG_BLOCK_ENTITY;
    public static void registerBlockEntities() {
        BREWING_KEG_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(UkrainianDelight.MOD_ID, "brewing_keg_be"),
                FabricBlockEntityTypeBuilder.create(BrewingKegBlockEntity::new,
                        ModBlocks.BREWING_KEG).build(null));

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, BREWING_KEG_BLOCK_ENTITY);
        UkrainianDelight.LOGGER.info("Registering Mod Block Entities for " + UkrainianDelight.MOD_ID);
    }
}
