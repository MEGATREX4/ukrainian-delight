package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class FoodBlockComponents {
    public static final FoodComponent APPLE_JAM = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f).build();
    public static final FoodComponent JARRED_TOMATOES = new FoodComponent.Builder().hunger(6).saturationModifier(0.6f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 3*20, 3), 1.0f).build();

}
