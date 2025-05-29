package com.megatrex4.ukrainian_dlight.datagen.providers;

import com.megatrex4.ukrainian_dlight.registry.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static com.megatrex4.ukrainian_dlight.registry.ItemsRegistry.LOAFBREAD;
import static net.minecraft.data.client.TextureMap.sideTopBottom;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(BlockRegistry.SALT_BLOCK);

        generator.registerSingleton(BlockRegistry.SALT_BAG, TexturedModel.ORIENTABLE_WITH_BOTTOM);
        registerCucumberCrop(generator);
    }


    private void registerCucumberCrop(BlockStateModelGenerator generator) {
        for (int age = 0; age <= 4; age++) {
            Identifier baseTexture = new Identifier("ukrainian_delight", "block/cucumber_crop/cucumber_crop_stage" + age);
            Models.CROP.upload(
                    new Identifier("ukrainian_delight", "block/cucumber_crop_stage" + age),
                    TextureMap.crop(baseTexture),
                    generator.modelCollector
            );

            if (age >= 2) {
                Identifier topTexture = new Identifier("ukrainian_delight", "block/cucumber_crop/cucumber_crop_stage" + age + "_top");
                Models.CROP.upload(
                        new Identifier("ukrainian_delight", "block/cucumber_crop_stage" + age + "_top"),
                        TextureMap.crop(topTexture),
                        generator.modelCollector
                );
            }
        }
    }







    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        ItemsRegistry.KRASHANKY_ITEMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        ItemsRegistry.PYSANKY_ITEMS.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
        itemModelGenerator.register(ItemsRegistry.CUCUMBER_SEEDS, Models.GENERATED);
        itemModelGenerator.register(ItemsRegistry.LOAFBREAD, Models.GENERATED);
        itemModelGenerator.register(ItemsRegistry.VERHUNY, Models.GENERATED);
    }
}
