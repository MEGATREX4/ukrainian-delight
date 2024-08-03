package com.megatrex4.ukrainian_dlight.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

public class FluidStack {
    public FluidVariant fluidVariant;
    public long amount;

    public FluidStack(FluidVariant variant, long amount) {
        this.fluidVariant = variant;
        this.amount = amount;
    }

    public static final long DROPLETS_PER_MILLIBUCKET = 81;

    public FluidVariant getFluidVariant() {
        return fluidVariant;
    }

    public void setFluidVariant(FluidVariant fluidVariant) {
        this.fluidVariant = fluidVariant;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public static long convertDropletsToMb(long droplets) {
        return droplets / DROPLETS_PER_MILLIBUCKET;
    }

    public static long convertMbToDroplets(long millibuckets) {
        return millibuckets * DROPLETS_PER_MILLIBUCKET;
    }
}
