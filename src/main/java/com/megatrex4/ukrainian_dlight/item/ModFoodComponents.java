package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ModFoodComponents {
    // Define identifiers for effects from other mods
    public static final Identifier COMFORT = new Identifier("farmersdelight", "comfort");
    public static final Identifier NOURISHMENT = new Identifier("farmersdelight", "nourishment");
    public static final Identifier SATURATION = new Identifier("minecraft", "saturation");

    // Define food components with multiple effects
    public static final FoodComponent VARENYK = createFoodComponent(3, 0.25f, COMFORT, 200, 0);
    public static final FoodComponent BORSCHT = createFoodComponent(4, 0.25f, NOURISHMENT, 200, 0);
    // public static final FoodComponent TOMATO = createFoodComponent(1, 0.1f);

    // Method to create food component with multiple effects
    public static FoodComponent createFoodComponent(int hunger, float saturation, @Nullable Identifier effectId, int duration, int amplifier) {
        if (effectId == null) {
            return createFoodComponent(hunger, saturation);
        }

        FoodComponent.Builder builder = new FoodComponent.Builder()
                .hunger(hunger)
                .saturationModifier(saturation);

        StatusEffect effect = Registries.STATUS_EFFECT.getOrEmpty(effectId).orElse(null);

        if (effect != null) {
            builder.statusEffect(new StatusEffectInstance(effect, duration, amplifier), 1.0F);
        } else {
            throw new IllegalArgumentException("Invalid effectId: " + effectId);
        }

        return builder.build();
    }

    // Overloaded method to create food component without effects
    public static FoodComponent createFoodComponent(int hunger, float saturation) {
        return new FoodComponent.Builder()
                .hunger(hunger)
                .saturationModifier(saturation)
                .build();
    }
}

