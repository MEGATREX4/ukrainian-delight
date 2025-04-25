package com.megatrex4.ukrainian_dlight.datagen.providers;

import com.megatrex4.ukrainian_dlight.registry.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import com.mojang.datafixers.util.Pair;

import static net.minecraft.data.client.TextureMap.sideTopBottom;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlockRegistry.SALT_BLOCK);

        blockStateModelGenerator.registerSingleton(BlockRegistry.SALT_BAG, TexturedModel.ORIENTABLE_WITH_BOTTOM);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        ItemsRegistry.KRASHANKY_ITEMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        ItemsRegistry.PYSANKY_ITEMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
    }
}
