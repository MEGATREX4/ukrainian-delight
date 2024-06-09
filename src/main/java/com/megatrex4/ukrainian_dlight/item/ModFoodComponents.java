package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModFoodComponents {
    // Define identifiers for effects from other mods
    public static final Identifier COMFORT = new Identifier("farmersdelight", "comfort");
    public static final Identifier NOURISHMENT = new Identifier("farmersdelight", "nourishment");
    // Define other identifiers as needed...

    // Define food components with multiple effects
    public static final FoodComponent VARENYK = createFoodComponent(3, 0.25f, List.of(new EffectData(COMFORT, 1500, 0)));
    public static final FoodComponent BORSCHT = createFoodComponent(5, 0.5f, List.of(new EffectData(NOURISHMENT, 3000, 0), new EffectData(COMFORT, 2000, 0)));
    public static final FoodComponent HORSERADISH = createFoodComponent(1, 0.1f, null);

    // Method to create food component with multiple optional effects
    public static FoodComponent createFoodComponent(int hunger, float saturation, @Nullable List<EffectData> effects) {
        FoodComponent.Builder builder = new FoodComponent.Builder()
                .hunger(hunger)
                .saturationModifier(saturation);

        if (effects != null) {
            for (EffectData effectData : effects) {
                // Use the effect ID directly
                Identifier effectId = effectData.effectId;
                StatusEffect effect = Registries.STATUS_EFFECT.getOrEmpty(effectId).orElse(null);
                if (effect != null) {
                    builder.statusEffect(new StatusEffectInstance(effect, effectData.duration, effectData.amplifier), 1.0F);
                    System.out.println("Adding effect: " + effectId);
                } else {
                    System.err.println("Effect not found: " + effectId);
                }
            }
        } else {
            System.out.println("No effects to add.");
        }

        return builder.build();
    }

    // Inner class to hold effect data
    public static class EffectData {
        public final Identifier effectId;
        public final int duration;
        public final int amplifier;

        public EffectData(Identifier effectId, int duration, int amplifier) {
            this.effectId = effectId;
            this.duration = duration;
            this.amplifier = amplifier;
        }
    }
}
