package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item VARENYK = registerItem("varenyk", createFoodItem(ModFoodComponents.VARENYK));
    public static final Item BORSCHT = registerItem("borscht", createFoodItem(ModFoodComponents.BORSCHT));
    public static final Item HORSERADISH = registerItem("horseradish", createFoodItem(ModFoodComponents.HORSERADISH));

    // Register items
    public static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    public static void registerModItems(){
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }

    private static Item createFoodItem(FoodComponent foodComponent) {
        System.out.println("Creating food item with component: " + foodComponent);
        return new CustomFoodItem(new FabricItemSettings().food(foodComponent));
    }
}
