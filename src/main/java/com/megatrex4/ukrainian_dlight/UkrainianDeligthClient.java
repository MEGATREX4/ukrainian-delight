package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.registry.BlockRegistry;
import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreen;
import com.megatrex4.ukrainian_dlight.registry.ScreenHandlersRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class UkrainianDeligthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlersRegistry.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.CUCUMBER_CROP, RenderLayer.getCutout());
    }
}
