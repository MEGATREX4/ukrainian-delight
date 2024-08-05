package com.megatrex4.ukrainian_dlight.block.entity.inventory;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BrewingKegResultSlot extends Slot {

    public final BrewingKegBlockEntity blockEntity;

    private final PlayerEntity player;
    private int removeCount;

    public BrewingKegResultSlot(PlayerEntity player, BrewingKegBlockEntity blockEntity, int index, int xPosition, int yPosition) {
        super(blockEntity, index, xPosition, yPosition);
        this.blockEntity = blockEntity;
        this.player = player;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (hasStack()) {
            removeCount += Math.min(amount, getStack().getCount());
        }
        return super.takeStack(amount);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        onCrafted(stack);
        super.onTakeItem(player, stack);
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        removeCount += amount;
        onCrafted(stack);
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        stack.onCraft(player.getWorld(), player, removeCount);

        if (!player.getWorld().isClient()) {
            blockEntity.clearUsedRecipes(player);
        }

        removeCount = 0;
    }

}
