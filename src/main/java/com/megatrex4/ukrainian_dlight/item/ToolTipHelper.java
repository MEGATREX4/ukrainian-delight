package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.util.StatusEffectUtil;
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

import java.util.List;

public class ToolTipHelper extends Item {

    public ToolTipHelper(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        FoodComponent foodComponent = stack.getItem().getFoodComponent();

        if (foodComponent != null && !foodComponent.getStatusEffects().isEmpty()) {
            // Use Pair from net.minecraft.util
            addFoodEffectTooltip(stack, tooltip, foodComponent.getStatusEffects());
        }
    }

    // Adjusted to accept List<com.mojang.datafixers.util.Pair<StatusEffectInstance, Float>>
    public static void addFoodEffectTooltip(ItemStack itemStack, List<Text> tooltip, List<com.mojang.datafixers.util.Pair<StatusEffectInstance, Float>> effects) {
        if (effects.isEmpty()) {
            tooltip.add(Text.translatable("tooltip.ukrainian_delight.no_effects").formatted(Formatting.GRAY));
        } else {
            for (com.mojang.datafixers.util.Pair<StatusEffectInstance, Float> pair : effects) {
                StatusEffectInstance effect = pair.getFirst();
                String name = effect.getEffectType().getTranslationKey();
                int duration = effect.getDuration() / 20; // Convert ticks to seconds
                int amplifier = effect.getAmplifier() + 1;
                String amplifierString = StatusEffectUtil.formatAmplifier(amplifier);
                String durationString = formatDuration(duration);

                Text tooltipText = Text.translatable(name).formatted(Formatting.BLUE).append(Text.of(amplifierString))
                        .append(Text.of(" (" + durationString + ") "));

                tooltip.add(tooltipText);
            }
        }
    }

    private static String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public static void registerTooltip() {
        UkrainianDelight.LOGGER.info("Registering ToolTip for " + UkrainianDelight.MOD_ID + " items");
    }
}
