package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class BrewingKegScreenHandler extends ScreenHandler {

    public static final int INGREDIENT_SLOT_1 = 0;
    public static final int INGREDIENT_SLOT_2 = 1;
    public static final int INGREDIENT_SLOT_3 = 2;
    public static final int INGREDIENT_SLOT_4 = 3;
    public static final int INGREDIENT_SLOT_5 = 4;
    public static final int INGREDIENT_SLOT_6 = 5;
    //adds container(like bottle of vial) input
    public static final int CONTAINER_SLOT = 6;
    //adds 1 input slot for water
    public static final int WATER_SLOT = 7;
    //adds 1 output slot and display slot
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public FluidStack fluidStack;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final BrewingKegBlockEntity blockEntity;

    public long getCapacity() {
        return blockEntity.getMaxWaterLevel();
    }

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

    public int getScaledWaterLevel() {
        long waterLevel = this.fluidStack.amount;
        long maxWaterLevel = this.blockEntity.getMaxWaterLevel();
        int waterBarHeight = 40; // This is the height in pixels of your water level indicator

        return maxWaterLevel != 0 && waterLevel != 0 ? (int)(waterLevel * waterBarHeight / maxWaterLevel) : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        if (index > this.slots.size() - 1) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = (Slot)this.slots.get(index);
            if (slot.hasStack()) {
                ItemStack slotItemStack = slot.getStack();
                itemStack = slotItemStack.copy();

                // Adjusting the slot handling based on your custom slots
                if (index == OUTPUT_SLOT) {
                    if (!this.insertItem(slotItemStack, 9, 45, true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index > OUTPUT_SLOT) {
                    if (slotItemStack.getItem() == Items.BOWL &&
                            !this.insertItem(slotItemStack, DRINKS_DISPLAY_SLOT, OUTPUT_SLOT, false) ||
                            !this.insertItem(slotItemStack, 0, 6, false) ||
                            !this.insertItem(slotItemStack, DRINKS_DISPLAY_SLOT, OUTPUT_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index == WATER_SLOT && slotItemStack.getItem() == Items.WATER_BUCKET) {
                    // Inserting water bucket into WATER_SLOT first
                    if (!this.insertItem(slotItemStack, WATER_SLOT, WATER_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index == WATER_SLOT) {
                    // If it's not a water bucket, but it's going into WATER_SLOT, try to insert
                    if (!this.insertItem(slotItemStack, WATER_SLOT + 1, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(slotItemStack, 9, 45, false)) {
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
