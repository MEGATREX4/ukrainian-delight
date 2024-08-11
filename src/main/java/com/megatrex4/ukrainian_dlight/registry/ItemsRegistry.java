package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.item.FoodItemBuilder;
import com.megatrex4.ukrainian_dlight.item.ModFoodComponents;
import com.megatrex4.ukrainian_dlight.item.KrashankyItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemsRegistry {

    // Define items using the ItemBuilder
    public static final Item VARENYK = registerItem("varenyk", new FoodItemBuilder()
                    .food(ModFoodComponents.VARENYK)
                    .build());

    public static final Item BORSCHT = registerItem("borscht", new FoodItemBuilder()
                    .food(ModFoodComponents.BORSCHT)
                    .maxCount(16)
                    .returnsBowl()
                    .build());

    public static final Item LEAN_BORSCHT = registerItem("lean_borscht", new FoodItemBuilder()
                    .food(ModFoodComponents.LEAN_BORSCHT)
                    .maxCount(16)
                    .returnsBowl()
                    .build());

    public static final Item HORSERADISH = registerItem("horseradish", new FoodItemBuilder()
                    .food(ModFoodComponents.HORSERADISH)
                    .build());

    public static final Item CUCUMBER = registerItem("cucumber", new FoodItemBuilder()
                    .food(ModFoodComponents.CUCUMBER)
                    .build());

    public static final Item CUTTED_CUCUMBER = registerItem("cutted_cucumber", new FoodItemBuilder()
                    .food(ModFoodComponents.CUTTED_CUCUMBER)
                    .build());

    public static final Item APPLE_SLICE = registerItem("apple_slice", new FoodItemBuilder()
                    .food(ModFoodComponents.APPLE_SLICE)
                    .build());

    public static final Item DRIED_APPLE_SLICE = registerItem("dried_apple_slice", new FoodItemBuilder()
                    .food(ModFoodComponents.DRIED_APPLE_SLICE)
                    .build());

    public static final Item HOMEMADE_SAUSAGE = registerItem("homemade_sausage", new FoodItemBuilder()
                    .food(ModFoodComponents.HOMEMADE_SAUSAGE)
                    .build());

    public static final Item CHERRY_BERRY = registerItem("cherry_berry", new FoodItemBuilder()
                    .food(ModFoodComponents.CHERRY_BERRY)
                    .build());

    public static final Item COTTAGE_CHEESE = registerItem("cottage_cheese", new FoodItemBuilder()
                    .food(ModFoodComponents.COTTAGE_CHEESE)
                    .build());

    public static final Item SALO = registerItem("salo", new FoodItemBuilder()
                    .food(ModFoodComponents.SALO)
                    .build());


    // Simple items without specifying maxCount, defaults to 64
    public static final Item SALT = registerItem("salt", new FoodItemBuilder().build());
    public static final Item YEAST = registerItem("yeast", new FoodItemBuilder().build());

    public static final Item BLACK_KRASHANKA = registerItem("black_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item BLUE_KRASHANKA = registerItem("blue_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item BROWN_KRASHANKA = registerItem("brown_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item CYAN_KRASHANKA = registerItem("cyan_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item GREEN_KRASHANKA = registerItem("green_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item GRAY_KRASHANKA = registerItem("gray_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item LIGHT_BLUE_KRASHANKA = registerItem("light_blue_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item LIGHT_GRAY_KRASHANKA = registerItem("light_gray_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item LIME_KRASHANKA = registerItem("lime_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item MAGENTA_KRASHANKA = registerItem("magenta_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item ORANGE_KRASHANKA = registerItem("orange_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item PINK_KRASHANKA = registerItem("pink_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item PURPLE_KRASHANKA = registerItem("purple_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item RED_KRASHANKA = registerItem("red_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item WHITE_KRASHANKA = registerItem("white_krashanka", new KrashankyItem(new FabricItemSettings()));
    public static final Item YELLOW_KRASHANKA = registerItem("yellow_krashanka", new KrashankyItem(new FabricItemSettings()));


    // Register items
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name.toLowerCase()), item);
    }

    public static void registerModItems() {
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }
}
