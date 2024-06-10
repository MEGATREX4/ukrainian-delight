package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.util.Language;

public class StatusEffectUtil {

    public static String formatAmplifier(int amplifier) {
        // Default to no suffix if amplifier is less than 1
        if (amplifier < 1) {
            return Language.getInstance().get("amplifier.none");
        }

        // Handle amplifiers greater than 5 by returning the number itself
        if (amplifier > 10) {
            return " " + amplifier;
        }

        // Fetch localized string for the amplifier
        String key = "amplifier." + amplifier;
        return Language.getInstance().get(key);
    }
}
