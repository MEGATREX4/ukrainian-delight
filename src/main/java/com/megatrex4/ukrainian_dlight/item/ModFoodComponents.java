package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.item.FoodComponents;
import vectorwing.farmersdelight.common.registry.ModEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;


public class ModFoodComponents {
    // Retrieve the StatusEffect instances using the EffectsRegistry
    public static final StatusEffect COMFORT = ModEffects.COMFORT.get();
    public static final StatusEffect NOURISHMENT = ModEffects.NOURISHMENT.get();

    // MEALS
    public static final FoodComponent VARENYK = new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(0.40f)
            .statusEffect(new StatusEffectInstance(COMFORT, 25*20, 0), 1.0F)
            .build();

    public static final FoodComponent BORSCHT = new FoodComponent.Builder()
            .hunger(15)
            .saturationModifier(1f)
            .statusEffect(new StatusEffectInstance(NOURISHMENT, 3*60*15, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(COMFORT, 5*60*15, 0), 1.0F)
            // .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 3*20, 3), 1.0f)
            .build();

    public static final FoodComponent LEAN_BORSCHT = new FoodComponent.Builder()
            .hunger(15)
            .saturationModifier(1f)
            .statusEffect(new StatusEffectInstance(NOURISHMENT, 3*60*15, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(COMFORT, 5*60*15, 0), 1.0F)
            .build();

    // INGREDIENTS
    public static final FoodComponent HORSERADISH = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.15f)
            .build();

    public static final FoodComponent CUCUMBER = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.15f)
            .build();

    public static final FoodComponent CUTTED_CUCUMBER = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1f)
            .build();

    public static final FoodComponent APPLE_SLICE = new FoodComponent.Builder()
            .hunger(3)
            .snack()
            .saturationModifier(0.2f)
            .build();

    public static final FoodComponent DRIED_APPLE_SLICE = new FoodComponent.Builder()
            .hunger(4)
            .snack()
            .saturationModifier(0.23f)
            .build();

    public static final FoodComponent HOMEMADE_SAUSAGE = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent CHERRY_BERRY = new FoodComponent.Builder()
            .hunger(1)
            .snack()
            .saturationModifier(0.1f)
            .build();

    public static final FoodComponent COTTAGE_CHEESE = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent SALO = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent APPLE_PYRIZHOK = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent CABBAGE_PYRIZHOK = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent MEAT_PYRIZHOK = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent POTATO_PYRIZHOK = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent BAKED_POTATOS_N_MUSHROOMS = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();

    public static final FoodComponent BAKED_PUMPKIN_SLICE = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.25f)
            .build();



    // Ensure that the class is initialized
    public static void init() {
        // This method is empty, but it ensures that the class is initialized
    }
}
