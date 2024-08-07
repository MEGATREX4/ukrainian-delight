package com.megatrex4.ukrainian_dlight.block.entity.inventory;


public class SlotInvalidRangeException extends RuntimeException {
    public SlotInvalidRangeException(int slotNumber, int maxRange) {
        super("Slot " + slotNumber + " not in valid range - [0," + maxRange + ")");
    }
}
