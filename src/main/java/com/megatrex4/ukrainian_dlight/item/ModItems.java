package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.megatrex4.ukrainian_dlight.item.ModFoodComponents;

public class ModItems {

    public static final Item VARENYK = registerItem("Varenyk", new CustomFoodItem(new FabricItemSettings().food(ModFoodComponents.VARENYK)));
    public static final Item BORSCHT = registerItem("Borscht", new CustomFoodItem(new FabricItemSettings().food(ModFoodComponents.BORSCHT)));
    // public static final Item TOMATO = registerItem("Tomato", new CustomFoodItem(new FabricItemSettings().food(ModFoodComponents.TOMATO)));



    // Register items
    public static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    public static void registerModItems(){
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }
}
