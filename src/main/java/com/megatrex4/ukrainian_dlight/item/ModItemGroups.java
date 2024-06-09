package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup UKRAINIAN_DELIGHT_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UkrainianDelight.MOD_ID, "ukrainian_delight"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.ukrainian_delight"))
                    .icon(() -> new ItemStack(ModItems.VARENYK))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.HORSERADISH);

                        entries.add(ModItems.VARENYK);
                        entries.add(ModItems.BORSCHT);
                    }).build());

    public static void registerItemGroups() {
        UkrainianDelight.LOGGER.info("Registering Item Groups for " + UkrainianDelight.MOD_ID);
    }
}
