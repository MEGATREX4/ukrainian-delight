package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.block.entity.inventory.BrewingKegResultSlot;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class BrewingKegScreenHandler extends ScreenHandler {

    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int WATER_SLOT = 7;
    public static final int DRINKS_DISPLAY_SLOT = 8;

    public static final int OUTPUT_SLOT = 9;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

    public FluidStack fluidStack;
    private final Inventory tileEntity;
    private final PropertyDelegate propertyDelegate;
    public final BrewingKegBlockEntity blockEntity;
    public final PlayerEntity player;

    public long getCapacity() {
        return blockEntity.getMaxWaterLevel();
    }

    public BrewingKegScreenHandler(int syncId, PlayerInventory tileEntity, PacketByteBuf buf) {
        this(syncId, tileEntity, (BrewingKegBlockEntity) tileEntity.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(INVENTORY_SIZE));
    }

    public BrewingKegScreenHandler(int syncId, PlayerInventory playerInventory, BrewingKegBlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.BREWING_KEG_SCREEN_HANDLER, syncId);
        checkSize(blockEntity, INVENTORY_SIZE); // Corrected type casting

        this.tileEntity = blockEntity; // Corrected type casting
        this.player = playerInventory.player;
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = blockEntity; // Corrected type casting

        // Ensure you provide capacity value
        long capacity = blockEntity.fluidStorage.getCapacity();
        this.fluidStack = new FluidStack(blockEntity.fluidStorage.variant, blockEntity.fluidStorage.amount);

        // Add 6 ingredient slots
        int inputStartX = 53;
        int inputStartY = 18;
        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                addSlot(new Slot(this.tileEntity, (row * 3) + column, inputStartX + (column * borderSlotSize), inputStartY + (row * borderSlotSize)));
            }
        }

        // Add container slot
        this.addSlot(new Slot(blockEntity, CONTAINER_SLOT, 97, 59));
        // Add water slot
        this.addSlot(new Slot(blockEntity, WATER_SLOT, 30, 59));

        // Add drinks display slot
        this.addSlot(new Slot(blockEntity, DRINKS_DISPLAY_SLOT, 131, 28) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });
        // Add output slot
        this.addSlot(new BrewingKegResultSlot(playerInventory.player, blockEntity, OUTPUT_SLOT, 131, 59));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(arrayPropertyDelegate);
    }

    public void setFluid(FluidStack stack) {
        fluidStack = stack;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 19;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledWaterLevel() {
        long waterLevel = FluidStack.convertDropletsToMb(this.fluidStack.amount);
        long maxWaterLevel = this.blockEntity.getMaxWaterLevel();
        int waterBarHeight = 40;

        return maxWaterLevel != 0 && waterLevel != 0 ? (int)(waterLevel * waterBarHeight / maxWaterLevel) : 0;
    }

    public void setInventory(ItemStack[] itemStacks) {
        for (int i = 0; i < itemStacks.length; i++) {
            this.slots.get(i).setStack(itemStacks[i]);
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        if (index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            // Handle item transfer from OUTPUT_SLOT to player inventory
            if (index == OUTPUT_SLOT) {
                if (!this.insertItem(slotItemStack, 11, Math.min(47, this.slots.size()), true)) { // Ensure range is within bounds
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotItemStack, itemStack);
            }
            // Prevent moving items into DRINKS_DISPLAY_SLOT
            else if (index == DRINKS_DISPLAY_SLOT) {
                return ItemStack.EMPTY;
            }
            // Handle item transfer from player inventory slots
            else if (index >= 11 && index < Math.min(47, this.slots.size())) { // Ensure range is within bounds
                if (slotItemStack.getItem() == Items.WATER_BUCKET) {
                    if (!this.insertItem(slotItemStack, WATER_SLOT, WATER_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(slotItemStack, 0, 6, false)) { // Adjusted range for ingredient slots
                    return ItemStack.EMPTY;
                }
            }
            // Handle transfer from ingredient slots to player inventory
            else if (index >= 0 && index < 6) { // INGREDIENT_SLOTS
                if (!this.insertItem(slotItemStack, 11, Math.min(47, this.slots.size()), false)) { // Ensure range is within bounds
                    return ItemStack.EMPTY;
                }
            }
            // Handle transfer for other slots
            else if (!this.insertItem(slotItemStack, 11, Math.min(47, this.slots.size()), false)) { // Ensure range is within bounds
                return ItemStack.EMPTY;
            }

            if (slotItemStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotItemStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(playerIn, slotItemStack);
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.tileEntity.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
