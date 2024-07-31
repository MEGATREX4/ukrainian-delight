package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.util.StatusEffectUtil;
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

        // Add food component tooltip
        if (foodComponent != null) {
            // Check for status effects and add them to the tooltip
            if (!foodComponent.getStatusEffects().isEmpty()) {
                for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
                    StatusEffectInstance instance = effect.getFirst();
                    String effectName = instance.getEffectType().getTranslationKey();
                    int duration = instance.getDuration() / 20;
                    int amplifier = instance.getAmplifier() + 1;
                    String amplifierString = StatusEffectUtil.formatAmplifier(amplifier);
                    String durationString = formatDuration(duration);
                    tooltip.add(Text.translatable(effectName).append(Text.of(" " + amplifierString)).append(Text.of(" (" + durationString + ") ")).formatted(Formatting.BLUE));
                }
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
