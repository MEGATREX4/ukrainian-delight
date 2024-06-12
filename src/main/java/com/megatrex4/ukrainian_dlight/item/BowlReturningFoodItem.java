package com.megatrex4.ukrainian_dlight.item;

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
        ItemStack resultStack = super.finishUsing(stack, world, user);

        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            ItemStack bowlStack = new ItemStack(Items.BOWL);

            boolean addedToInventory = player.getInventory().insertStack(bowlStack);

            if (!addedToInventory) {
                player.dropItem(bowlStack, false);
            }
        }

        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }
}
