package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {

    public static FoodComponent createFoodComponent(int hunger, float saturation, StatusEffectInstance... effects) {
        FoodComponent.Builder builder = new FoodComponent.Builder()
                .hunger(hunger)
                .saturationModifier(saturation);

        if (effects != null) {
            for (StatusEffectInstance effect : effects) {
                if (effect != null) {
                    builder.statusEffect(effect, 1.0F);
                }
            }
        }

        return builder.build();
    }
}
