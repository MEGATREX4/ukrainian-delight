package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class BrewingKegScreenHandler extends ScreenHandler {

    public FluidStack fluidStack;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final BrewingKegBlockEntity blockEntity;

    public BrewingKegScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(10));
    }

    public BrewingKegScreenHandler(int syncId, PlayerInventory playerInventory,
                                   BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.BREWING_KEG_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 10);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((BrewingKegBlockEntity) blockEntity);
        this.fluidStack = new FluidStack(((BrewingKegBlockEntity) blockEntity).fluidStorage.variant, ((BrewingKegBlockEntity) blockEntity).fluidStorage.amount);

        //add 6 ingredients slots
        int inputStartX = 53;
        int inputStartY = 18;
        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                addSlot(new Slot(inventory, (row * 3) + column,
                        inputStartX + (column * borderSlotSize),
                        inputStartY + (row * borderSlotSize)));
            }
        }

        //adds container(like bottle of vial) input
        this.addSlot(new Slot(inventory, 6, 97, 59));
        //adds 1 input slot for water
        this.addSlot(new Slot(inventory, 7, 30, 59));
        //adds 1 display slot that does not allow item insertion or extraction
        this.addSlot(new Slot(inventory, 8, 131, 28) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });
        //adds 1 output slot that only allows item extraction
        this.addSlot(new Slot(inventory, 9, 131, 59) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

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
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 19; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
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
