package com.megatrex4.ukrainian_dlight.compat.jade;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.custom.BrewingKegBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class UkrainianDelightJade implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        UkrainianDelight.LOGGER.info("Jade compat for " + UkrainianDelight.MOD_ID + "!");
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        JadeCompat jadeCompat = new JadeCompat();
        registration.registerBlockComponent(jadeCompat, BrewingKegBlock.class);
    }
}
