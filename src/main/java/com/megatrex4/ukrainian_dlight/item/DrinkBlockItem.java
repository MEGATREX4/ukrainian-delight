package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.registry.ModTags;
import com.megatrex4.ukrainian_dlight.util.StatusEffectUtil;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DrinkBlockItem extends BlockItem {
    private final FoodComponent foodComponent;
    private final List<EndTickHandler> activeHandlers = new ArrayList<>();

    public DrinkBlockItem(Block block, FoodComponent foodComponent) {
        super(block, new Settings().food(foodComponent));
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

            // Apply the main effects
            for (Pair<StatusEffectInstance, Float> pair : this.foodComponent.getStatusEffects()) {
                if (world.isClient || pair.getFirst() == null || !(world.getRandom().nextFloat() < pair.getSecond())) {
                    continue;
                }
                applyMainEffect(player, pair.getFirst());
            }

            // Handle item stack update manually
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            // Schedule secondary effects after the main effect duration ends
            if (!world.isClient && world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;
                for (Pair<StatusEffectInstance, Float> pair : this.foodComponent.getStatusEffects()) {
                    if (pair.getFirst() != null) {
                        int duration = pair.getFirst().getDuration();
                        EndTickHandler handler = new EndTickHandler(serverWorld.getServer(), player, pair.getFirst(), duration);
                        activeHandlers.add(handler); // Add handler to the active list
                        ServerTickEvents.END_SERVER_TICK.register(handler);
                    }
                }
            }
        }

        return stack.isEmpty() ? new ItemStack(this.getBlock()) : stack; // Return the updated stack
    }

    class EndTickHandler implements ServerTickEvents.EndTick {
        private final MinecraftServer server;
        private final PlayerEntity player;
        private final StatusEffectInstance effect;
        private int ticksLeft;
        private boolean applied;

        public EndTickHandler(MinecraftServer server, PlayerEntity player, StatusEffectInstance effect, int duration) {
            this.server = server;
            this.player = player;
            this.effect = effect;
            this.ticksLeft = duration;
            this.applied = false;
        }

        @Override
        public void onEndTick(MinecraftServer server) {
            if (this.server != server) return; // Ensure we are on the correct server
            if (ticksLeft-- <= 0 && !applied) {
                StatusEffectUtil.applySecondaryEffects(player, effect);
                applied = true; // Mark that effects have been applied

                // Manually remove the handler from the active list
                activeHandlers.remove(this);

                // Optionally, you can unregister from the event here if it supports it
                ServerTickEvents.END_SERVER_TICK.equals(this);
            }
        }
    }



    private void applyMainEffect(PlayerEntity player, StatusEffectInstance effect) {
        player.addStatusEffect(new StatusEffectInstance(effect));
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
                    tooltip.add(Text.translatable(effectName).append(Text.of(" " + amplifierString)).append(Text.of(" (" + durationString + ") ")).formatted(Formatting.BLUE));
                }
            }
        }

        // Check if the item is in the drink tags and add corresponding tooltip
        if (stack.isIn(ModTags.LIGHT_DRINK)) {
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            tooltip.add(UkrainianDelight.i18n("tooltip.light_drink").formatted(Formatting.GREEN));
        } else if (stack.isIn(ModTags.MID_DRINK)) {
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            tooltip.add(UkrainianDelight.i18n("tooltip.mid_drink").formatted(Formatting.YELLOW));
        } else if (stack.isIn(ModTags.STRONG_DRINK)) {
            tooltip.add(Text.literal("").formatted(Formatting.GRAY));
            tooltip.add(UkrainianDelight.i18n("tooltip.strong_drink").formatted(Formatting.RED));
        }
    }

    private static String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}
