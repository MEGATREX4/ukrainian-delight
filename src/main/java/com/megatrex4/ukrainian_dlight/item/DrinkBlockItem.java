package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.util.StatusEffectUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class DrinkBlockItem extends BlockItem {
    private final FoodComponent foodComponent;

    public DrinkBlockItem(Block block, FoodComponent foodComponent) {
        super(block, new Item.Settings().food(foodComponent));
        this.foodComponent = foodComponent;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.canConsume(this.foodComponent.isAlwaysEdible())) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack); // Consume the item
        } else {
            return TypedActionResult.fail(itemStack); // Fail if cannot consume
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            player.getHungerManager().eat(this, stack); // Consume the item
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1.0F, 1.0F); // Play drinking sound
        }

        return super.finishUsing(stack, world, user); // Return the modified stack after consumption
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK; // Set the use action to DRINK for drinking behavior
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32; // Set a fixed max use time for drinking animation
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        // Add food component tooltip
        if (foodComponent != null) {
            // Check for status effects and add them to the tooltip
            if (!foodComponent.getStatusEffects().isEmpty()) {
                for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
                    StatusEffectInstance instance = effect.getFirst();
                    String effectName = instance.getEffectType().getName().getString();
                    int duration = instance.getDuration() / 20;
                    int amplifier = instance.getAmplifier() + 1;
                    String amplifierString = StatusEffectUtil.formatAmplifier(amplifier);
                    String durationString = formatDuration(duration);
                    tooltip.add(Text.translatable(effectName).append(Text.of(amplifierString)).append(Text.of(" (" + durationString + ") ")).formatted(Formatting.BLUE));
                }
            }
        }
    }

    private static String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}
