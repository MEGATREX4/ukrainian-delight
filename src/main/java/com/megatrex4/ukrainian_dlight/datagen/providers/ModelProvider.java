package com.megatrex4.ukrainian_dlight.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.client.Models;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // No block state models to generate for Krashanka items
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        ItemsRegistry.KRASHANKY_ITEMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
    }
}
