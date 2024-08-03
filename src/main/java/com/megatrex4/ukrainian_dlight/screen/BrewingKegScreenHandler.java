package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
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
    public static final int WATER_SLOT = 8;
    public static final int DRINKS_DISPLAY_SLOT = 9;

    public static final int OUTPUT_SLOT = 10;

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
        this(syncId, tileEntity, tileEntity.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(INVENTORY_SIZE));
    }

    public BrewingKegScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.BREWING_KEG_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), INVENTORY_SIZE);

        this.tileEntity = ((Inventory) blockEntity);
        this.player = playerInventory.player;
        tileEntity.onOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((BrewingKegBlockEntity) blockEntity);

        // Ensure you provide capacity value
        long capacity = ((BrewingKegBlockEntity) blockEntity).fluidStorage.getCapacity(); // Updated line
        this.fluidStack = new FluidStack(((BrewingKegBlockEntity) blockEntity).fluidStorage.variant, ((BrewingKegBlockEntity) blockEntity).fluidStorage.amount);

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
        this.addSlot(new Slot(tileEntity, CONTAINER_SLOT, 97, 59));
        // Add water slot
        this.addSlot(new Slot(tileEntity, WATER_SLOT, 30, 59));


    // Add drinks display slot
        this.addSlot(new Slot(tileEntity, DRINKS_DISPLAY_SLOT, 131, 28) {
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
        this.addSlot(new Slot(tileEntity, OUTPUT_SLOT, 131, 59) {
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

    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        if (index > this.slots.size() - 1) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(index);
            if (slot.hasStack()) {
                ItemStack slotItemStack = slot.getStack();
                itemStack = slotItemStack.copy();

                if (index == OUTPUT_SLOT) {
                    // Transfer items from OUTPUT_SLOT to player inventory
                    if (!this.insertItem(slotItemStack, 11, 47, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickTransfer(slotItemStack, itemStack);
                } else if (index >= 11 && index < 47) {
                    // Transfer items from player inventory
                    if (slotItemStack.getItem() == Items.WATER_BUCKET) {
                        if (!this.insertItem(slotItemStack, WATER_SLOT, WATER_SLOT + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(slotItemStack, 0, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // For items in container slots, transfer to player inventory
                    if (!this.insertItem(slotItemStack, 11, 47, false)) {
                        return ItemStack.EMPTY;
                    }
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
