package com.megatrex4.ukrainian_dlight.util;

import io.github.fabricators_of_create.porting_lib.mixin.common.ResourceLocationMixin;
import net.minecraft.util.Identifier;

import static com.megatrex4.ukrainian_dlight.UkrainianDelight.MOD_ID;

public class UDIdentifier extends Identifier {
    public UDIdentifier(String path) {
        super(MOD_ID, path);
    }
}
