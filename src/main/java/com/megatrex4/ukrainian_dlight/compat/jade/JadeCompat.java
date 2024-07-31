package com.megatrex4.ukrainian_dlight.compat.jade;

import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class JadeCompat implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        // Your implementation
    }

    @Override
    public Identifier getUid() {
        return new Identifier("ukrainian_dlight", "brewing_keg");
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public int getDefaultPriority() {
        return -9900;
    }
}
