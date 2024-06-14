package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Define items using the ItemBuilder
    public static final Item VARENYK = registerItem("varenyk", new ItemBuilder()
                    .food(ModFoodComponents.VARENYK)
                    .build());

    public static final Item BORSCHT = registerItem("borscht", new ItemBuilder()
                    .food(ModFoodComponents.BORSCHT)
                    .maxCount(16)
                    .returnsBowl()
                    .build());

    public static final Item LEAN_BORSCHT = registerItem("lean_borscht", new ItemBuilder()
                    .food(ModFoodComponents.LEAN_BORSCHT)
                    .maxCount(16)
                    .returnsBowl()
                    .build());

    public static final Item HORSERADISH = registerItem("horseradish", new ItemBuilder()
                    .food(ModFoodComponents.HORSERADISH)
                    .build());

    public static final Item CUCUMBER = registerItem("cucumber", new ItemBuilder()
                    .food(ModFoodComponents.CUCUMBER)
                    .build());

    public static final Item CUTTED_CUCUMBER = registerItem("cutted_cucumber", new ItemBuilder()
                    .food(ModFoodComponents.CUTTED_CUCUMBER)
                    .build());

    public static final Item APPLE_SLICE = registerItem("apple_slice", new ItemBuilder()
                    .food(ModFoodComponents.APPLE_SLICE)
                    .build());

    public static final Item DRIED_APPLE_SLICE = registerItem("dried_apple_slice", new ItemBuilder()
                    .food(ModFoodComponents.DRIED_APPLE_SLICE)
                    .build());

    public static final Item HOMEMADE_SAUSAGE = registerItem("homemade_sausage", new ItemBuilder()
                    .food(ModFoodComponents.HOMEMADE_SAUSAGE)
                    .build());

    // Simple items without specifying maxCount, defaults to 64
    public static final Item SALT = registerItem("salt", new ItemBuilder().build());

    // Register items
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    public static void registerModItems() {
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }
}
