package com.megatrex4.ukrainian_dlight.util;

import com.megatrex4.ukrainian_dlight.registry.TagsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

public class StatusEffectUtil {

    private static final Random RANDOM = new Random();

    public static void applySecondaryEffects(PlayerEntity player, StatusEffectInstance mainEffect) {
        if (mainEffect == null) {
            return;
        }

        int duration = mainEffect.getDuration();
        int amplifier = mainEffect.getAmplifier() + 1;

        if (player.getInventory().contains(TagsRegistry.Items.LIGHT_DRINK)) {
            applyWeakness(player, duration / 2, (int) (amplifier * 1.3));
            applyNausea(player, duration / 2, (int) (amplifier * 1.3));
            if (RANDOM.nextFloat() < 0.2) {
                applyBlindness(player, (int) (duration * 0.7), (int) (amplifier * 1.3));
            }
        } else if (player.getInventory().contains(TagsRegistry.Items.MID_DRINK)) {
            applyWeakness(player, (int) (duration * 0.7), (int) (amplifier * 1.5));
            applyNausea(player, (int) (duration * 0.7), (int) (amplifier * 1.5));
            if (RANDOM.nextFloat() < 0.7) {
                applyBlindness(player, (int) (duration * 0.7), (int) (amplifier * 1.5));
            }
        } else if (player.getInventory().contains(TagsRegistry.Items.STRONG_DRINK)) {
            applyWeakness(player, (int) (duration * 0.9), (int) (amplifier * 1.7));
            applyNausea(player, (int) (duration * 0.9), (int) (amplifier * 1.7));
            if (RANDOM.nextFloat() < 0.8) {
                applyBlindness(player, (int) (duration * 0.9), (int) (amplifier * 2.2));
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
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] suffixes = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (amplifier >= values[i]) {
                amplifier -= values[i];
                result.append(suffixes[i]);
            }
        }
        return result.toString();
    }
}
