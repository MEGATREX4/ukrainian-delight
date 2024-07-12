package com.megatrex4.ukrainian_dlight.util;

import com.megatrex4.ukrainian_dlight.registry.ModTags;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Random;

public class StatusEffectUtil {

    private static final Random RANDOM = new Random();

    public static void applySecondaryEffects(PlayerEntity player, StatusEffectInstance mainEffect) {
        if (mainEffect == null) {
            return;
        }

        int duration = mainEffect.getDuration();
        int amplifier = mainEffect.getAmplifier() + 1;

        if (player.getInventory().contains(ModTags.LIGHT_DRINK)) {
            applyWeakness(player, duration / 2, (int) (amplifier * 1.3)); // Convert to int after multiplication
            applyNausea(player, duration / 2, (int) (amplifier * 1.3)); // Convert to int after multiplication
        } else if (player.getInventory().contains(ModTags.MID_DRINK)) {
            applyWeakness(player, (int) (duration * 0.7), (int) (amplifier * 1.7)); // Convert to int after multiplication
            applyNausea(player, (int) (duration * 0.7), (int) (amplifier * 1.7)); // Convert to int after multiplication
            if (RANDOM.nextFloat() < 0.5) {
                applyBlindness(player, (int) (duration * 0.7), (int) (amplifier * 1.7)); // Convert to int after multiplication
            }
        } else if (player.getInventory().contains(ModTags.STRONG_DRINK)) {
            applyWeakness(player, (int) (duration * 0.9), (int) (amplifier * 2.2)); // Convert to int after multiplication
            applyNausea(player, (int) (duration * 0.9), (int) (amplifier * 2.2)); // Convert to int after multiplication
            if (RANDOM.nextFloat() < 0.7) {
                applyBlindness(player, (int) (duration * 0.9), (int) (amplifier * 2.2)); // Convert to int after multiplication
            }
        }
    }


    private static void applyWeakness(PlayerEntity player, int duration, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, amplifier));
    }

    private static void applyNausea(PlayerEntity player, int duration, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, duration, amplifier));
    }

    private static void applyBlindness(PlayerEntity player, int duration, int amplifier) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, amplifier));
    }

    public static String formatAmplifier(int amplifier) {
        // Default to no suffix if amplifier is less than 1
        if (amplifier < 1) {
            return "I";
        }

        // Handle amplifiers greater than 5 by returning the number itself
        if (amplifier > 10) {
            return "  " + amplifier;
        }

        // Fetch localized string for the amplifier
        String[] suffixes = {"I", "II", "III", "IV", "V"};
        return suffixes[amplifier - 1];
    }
}
