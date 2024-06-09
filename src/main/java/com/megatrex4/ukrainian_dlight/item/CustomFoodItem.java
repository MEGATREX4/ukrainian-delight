package com.megatrex4.ukrainian_dlight.item;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CustomFoodItem extends Item {

    public CustomFoodItem(Settings settings) {
        super(settings);
    }

    // This method adds tooltip information to the item when hovered over in the inventory
    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        // Retrieve the FoodComponent of the item
        FoodComponent foodComponent = this.getFoodComponent();

        // Check if the FoodComponent exists and if it has any status effects
        if (foodComponent != null && !foodComponent.getStatusEffects().isEmpty()) {
            // Call a method to add the status effects to the tooltip
            addFoodEffectTooltip(stack, tooltip, foodComponent.getStatusEffects());
        }
    }

    // This method adds status effect information to the tooltip
    public static void addFoodEffectTooltip(ItemStack itemStack, List<Text> tooltip, List<Pair<StatusEffectInstance, Float>> effects) {
        // If there are no effects, add a message indicating that there are no effects
        if (effects.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.ukrainian_delight.no_effects").formatted(Formatting.GRAY));
        } else {
            // Iterate through each effect and add its information to the tooltip
            for (Pair<StatusEffectInstance, Float> pair : effects) {
                StatusEffectInstance effect = pair.getFirst();
                String name = effect.getEffectType().getTranslationKey();
                int duration = effect.getDuration() / 20; // Convert ticks to seconds
                int amplifier = effect.getAmplifier();
                String amplifierString = formatAmplifier(amplifier + 1);
                String durationString = formatDuration(duration);

                // Combine the effect name and duration, formatting them as blue text
                Text tooltipText = Text.translatable(name).formatted(Formatting.BLUE).append(Text.of(" " + amplifierString))
                        .append(Text.of(" (" + durationString + ") "));

                // Add the tooltip text to the list
                tooltip.add(tooltipText);
            }
        }
    }



    private static String formatAmplifier(int amplifier) {
        // amplifier to roman numeral
        String romanNumeral = "";
        switch (amplifier) {
            case 1:
                romanNumeral = "I";
                break;
            case 2:
                romanNumeral = "II";
                break;
            case 3:
                romanNumeral = "III";
                break;
            case 4:
                romanNumeral = "IV";
                break;
            case 5:
                romanNumeral = "V";
                break;
        }
        return romanNumeral;
    }


    // This method formats the duration of an effect
    private static String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}