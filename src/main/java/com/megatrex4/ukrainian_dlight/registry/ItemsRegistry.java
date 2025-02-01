package com.megatrex4.ukrainian_dlight.registry;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.item.FoodItemBuilder;
import com.megatrex4.ukrainian_dlight.item.ModFoodComponents;
import com.megatrex4.ukrainian_dlight.item.KrashankyItem;
import com.megatrex4.ukrainian_dlight.util.UDIdentifier;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;

import static com.megatrex4.ukrainian_dlight.item.ModFoodComponents.*;

public class ItemsRegistry {


    public static final Item VARENYK = registerFoodItem("varenyk", createFoodComponent(6, 0.40f), 64, false, false);
    public static final Item BORSCHT = registerFoodItem("borscht", createFoodComponent(15, 1f, new StatusEffectInstance(ModEffects.COMFORT.get(), 5 * 60 * 20), new StatusEffectInstance(ModEffects.NOURISHMENT.get(), 3 * 60 * 20)), 16, true, false);
    public static final Item LEAN_BORSCHT = registerFoodItem("lean_borscht", createFoodComponent(8, 0.8f, new StatusEffectInstance(ModEffects.COMFORT.get(), 3 * 60 * 20)), 16, true, false);
    public static final Item HORSERADISH = registerFoodItem("horseradish", createFoodComponent(6, 0.15f), 64, false, false);
    public static final Item CUCUMBER = registerFoodItem("cucumber", createFoodComponent(2, 0.3f), 64, false, false);
    public static final Item HOMEMADE_SAUSAGE = registerFoodItem("homemade_sausage", createFoodComponent(7, 0.6f), 64, false, false);
    public static final Item CUTTED_CUCUMBER = registerFoodItem("cutted_cucumber", createFoodComponent(1, 0.1f), 64, false, true);
    public static final Item APPLE_SLICE = registerFoodItem("apple_slice", createFoodComponent(3, 0.2f), 64, false, true);
    public static final Item DRIED_APPLE_SLICE = registerFoodItem("dried_apple_slice", createFoodComponent(3, 0.2f), 64, false, true);
    public static final Item CHERRY_BERRY = registerFoodItem("cherry_berry", createFoodComponent(3, 0.2f), 64, false, true);
    public static final Item COTTAGE_CHEESE = registerFoodItem("cottage_cheese", createFoodComponent(3, 0.2f), 64, false, false);
    public static final Item SALO = registerFoodItem("salo", createFoodComponent(3, 0.2f), 64, false, false);


    // Simple items without specifying maxCount, defaults to 64
    public static final Item SALT = registerItem("salt", new FoodItemBuilder().build());
    public static final Item YEAST = registerItem("yeast", new FoodItemBuilder().build());

    //KRASHANKY
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

    public static final ImmutableList<Item> KRASHANKY_ITEMS = ImmutableList.of(
            BLACK_KRASHANKA,
            BLUE_KRASHANKA,
            BROWN_KRASHANKA,
            CYAN_KRASHANKA,
            GREEN_KRASHANKA,
            GRAY_KRASHANKA,
            LIGHT_BLUE_KRASHANKA,
            LIGHT_GRAY_KRASHANKA,
            LIME_KRASHANKA,
            MAGENTA_KRASHANKA,
            ORANGE_KRASHANKA,
            PINK_KRASHANKA,
            PURPLE_KRASHANKA,
            RED_KRASHANKA,
            WHITE_KRASHANKA,
            YELLOW_KRASHANKA
    );

    // Register items
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new UDIdentifier(name.toLowerCase()), item);
    }

    public static void registerModItems() {
        UkrainianDelight.LOGGER.info("Registering Mod Items for " + UkrainianDelight.MOD_ID);
    }

    // Main method with the optional Identifier parameter
    private static Item registerFoodItem(String name, FoodComponent foodComponent, int maxCount, boolean returnsBowl, boolean isSnack, Identifier bowlId) {
        FoodItemBuilder builder = new FoodItemBuilder()
                .food(foodComponent)
                .maxCount(maxCount);

        if (returnsBowl) {
            builder.returnsBowl(bowlId);
        }
        if (isSnack) builder.snack();

        return registerItem(name, builder.build());
    }

    // Overloaded method to handle the case where the bowl identifier is not provided (returns default bowl)
    private static Item registerFoodItem(String name, FoodComponent foodComponent, int maxCount, boolean returnsBowl, boolean isSnack) {
        return registerFoodItem(name, foodComponent, maxCount, returnsBowl, isSnack, new Identifier("minecraft", "bowl"));
    }
}
