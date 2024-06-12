package com.megatrex4.ukrainian_dlight.item;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class BowlReturningFoodItem extends ToolTipHelper {

    public BowlReturningFoodItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // Use the item as usual
        ItemStack resultStack = super.finishUsing(stack, world, user);

        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            ItemStack bowlStack = new ItemStack(Items.BOWL);

            // Check if the player's inventory can accept the bowl
            boolean addedToInventory = player.getInventory().insertStack(bowlStack);

            // Log inventory addition attempt
            // UkrainianDelight.LOGGER.info("Attempting to add bowl to inventory: " + addedToInventory);

            // If not added to inventory, drop the bowl near the player
            if (!addedToInventory) {
                player.dropItem(bowlStack, false);
                // UkrainianDelight.LOGGER.info("Dropping bowl near player");
            }
        }

        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }
}
