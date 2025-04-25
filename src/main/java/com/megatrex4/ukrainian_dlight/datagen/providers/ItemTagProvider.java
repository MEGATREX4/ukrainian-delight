package com.megatrex4.ukrainian_dlight.datagen.providers;

import com.megatrex4.ukrainian_dlight.item.DrinkBlockItem;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import com.megatrex4.ukrainian_dlight.registry.TagsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (Item item : ItemsRegistry.KRASHANKY_ITEMS) {
            getOrCreateTagBuilder(TagsRegistry.Items.KRASHANKA).add(item);
        }

        for (Item item : ItemsRegistry.PYSANKY_ITEMS) {
            getOrCreateTagBuilder(TagsRegistry.Items.PYSANKA).add(item);
        }

        getOrCreateTagBuilder(TagsRegistry.Items.SALT).add(ItemsRegistry.SALT);
    }
}
