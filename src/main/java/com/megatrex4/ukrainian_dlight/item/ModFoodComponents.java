package com.megatrex4.ukrainian_dlight.item;

import com.nhoryzon.mc.farmersdelight.registry.EffectsRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFoodComponents {
    // Retrieve the StatusEffect instances using the EffectsRegistry
    public static final StatusEffect COMFORT = EffectsRegistry.COMFORT.get();
    public static final StatusEffect NOURISHMENT = EffectsRegistry.NOURISHMENT.get();

    // Define food components with multiple effects
    public static final FoodComponent VARENYK = new FoodComponent.Builder()
            .hunger(3)
            .saturationModifier(0.35f)
            .statusEffect(new StatusEffectInstance(COMFORT, 25*20, 0), 1.0F)
            .snack()
            .build();

    public static final FoodComponent BORSCHT = new FoodComponent.Builder()
            .hunger(15)
            .saturationModifier(1f)
            .statusEffect(new StatusEffectInstance(NOURISHMENT, 5*60*20, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(COMFORT, 5*60*20, 0), 1.0F)
            // .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 3*20, 3), 1.0f)
            .build();

    public static final FoodComponent HORSERADISH = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1f)
            .snack()
            .build();

    // Ensure that the class is initialized
    public static void init() {
        // This method is empty, but it ensures that the class is initialized
    }
}
