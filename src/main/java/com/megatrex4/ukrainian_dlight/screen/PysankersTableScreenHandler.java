package com.megatrex4.ukrainian_dlight.screen;

import com.megatrex4.ukrainian_dlight.block.custom.PysankerTableBlock;
import com.megatrex4.ukrainian_dlight.registry.PatternRegistry;
import com.megatrex4.ukrainian_dlight.registry.ScreenHandlersRegistry;
import com.megatrex4.ukrainian_dlight.registry.manager.PatternManager;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

import java.util.List;

public class PysankersTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ScreenHandlerContext context;

    // Slot indices
    public static final int KRASHANKA_SLOT = 0;
    public static final int DYE_SLOT = 1;
    public static final int PATTERN_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    // Inventory size
    public static final int INVENTORY_SIZE = 4; // 4 slots for krashanka, dye, pattern, and output

    private final Property selectedPattern;
    private List<RegistryEntry<PatternRegistry>> patterns;
    private Runnable inventoryChangeListener;

    public PysankersTableScreenHandler(int syncId, PlayerInventory playerInventory, PysankerTableBlock block, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlersRegistry.PYSANKERS_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, INVENTORY_SIZE);
        this.selectedPattern = Property.create();
        this.patterns = List.of();
        this.inventoryChangeListener = () -> {};

        this.context = context;

        // Add slots for the Pysankers Table
        this.addSlot(new Slot(inventory, KRASHANKA_SLOT, 13, 26));  // Input slot for krashanka
        this.addSlot(new Slot(inventory, DYE_SLOT, 33, 26));       // Dye slot
        this.addSlot(new Slot(inventory, PATTERN_SLOT, 23, 45));    // Pattern slot
        this.addSlot(new Slot(inventory, OUTPUT_SLOT, 143, 58));    // Output slot

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Example method for applying a pattern to krashanka
    public void applyPattern(ItemStack krashanka, ItemStack patternItem) {
        Identifier patternId = PatternManager.getPatternForItem(patternItem).getId();
        NbtCompound nbt = krashanka.getOrCreateNbt();
        NbtList patterns;
        if (nbt.contains("Patterns", NbtType.LIST)) {
            patterns = nbt.getList("Patterns", NbtType.COMPOUND);
        } else {
            patterns = new NbtList();
            nbt.put("Patterns", patterns);
        }
        NbtCompound patternData = new NbtCompound();
        patternData.putString("Pattern", patternId.toString());
        patterns.add(patternData);
        krashanka.setNbt(nbt);
        this.slots.get(OUTPUT_SLOT).setStack(krashanka.copy()); // Update output slot
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        Slot slot = this.slots.get(slotId);
        if (slot == null || !slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getStack();
        ItemStack stackCopy = stack.copy();

        if (slotId < INVENTORY_SIZE) {
            // Move from container to player inventory
            if (!this.insertItem(stack, INVENTORY_SIZE, INVENTORY_SIZE + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Move from player inventory to container
            if (!this.insertItem(stack, 0, INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return stackCopy;
    }


    @Override
    public void onSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotId >= 0 && slotId < this.slots.size()) {
            Slot slot = this.slots.get(slotId);
            if (slot != null) {
                if (slotId == PATTERN_SLOT) {
                    ItemStack patternItem = slot.getStack();
                    ItemStack krashankaItem = this.getSlot(KRASHANKA_SLOT).getStack();

                    // Apply the pattern to the krashanka item
                    applyPattern(krashankaItem, patternItem);
                    this.slots.get(OUTPUT_SLOT).setStack(krashankaItem.copy()); // Update output slot
                } else {
                    super.onSlotClick(slotId, button, actionType, player); // Call parent method for default handling
                }
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        // Add slots for the player's inventory
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        // Add slots for the player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
