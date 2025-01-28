package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import net.minecraft.util.Identifier;

public class BowlReturningFoodItem extends ToolTipHelper {
    private final Identifier bowlId; // Store the bowl identifier

    public BowlReturningFoodItem(Settings settings, Identifier bowlId) {
        super(settings);
        this.bowlId = bowlId; // Initialize with the bowl identifier
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack resultStack = super.finishUsing(stack, world, user);

        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;
            Item bowlItem = Registries.ITEM.get(bowlId); // Retrieve the bowl item based on the identifier
            ItemStack bowlStack = new ItemStack(bowlItem);

            boolean addedToInventory = player.getInventory().insertStack(bowlStack);

            if (!addedToInventory) {
                player.dropItem(bowlStack, false);
            }
        }

        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }
}
