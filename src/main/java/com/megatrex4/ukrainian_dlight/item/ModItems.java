package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Define items without specifying maxCount, defaults to 64
    public static final Item VARENYK = createFoodItem("varenyk", ModFoodComponents.VARENYK);
    public static final Item BORSCHT = createFoodItem("borscht", ModFoodComponents.BORSCHT, 16);
    public static final Item HORSERADISH = createFoodItem("horseradish", ModFoodComponents.HORSERADISH);

    // Simple items without specifying maxCount, defaults to 64
    public static final Item SALT = registerItem("salt", new Item(new FabricItemSettings()));

    // Register items with optional maxCount
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    public static Item registerItem(String name, Item item, int maxCount) {
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    // Create food item with specified food component, optional maxCount
    private static Item createFoodItem(String name, FoodComponent foodComponent) {
        return createFoodItem(name, foodComponent, 64);
    }

    private static Item createFoodItem(String name, FoodComponent foodComponent, int maxCount) {
        // Ensure that ModFoodComponents is initialized before accessing its components
        ModFoodComponents.init();

        // Create the food item with the specified max count
        return registerItem(name, new ToolTipHelper(new FabricItemSettings().food(foodComponent).maxCount(maxCount)), maxCount);
    }

    public static void registerModItems() {
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }
}
