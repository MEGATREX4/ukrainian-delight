package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreen;
import com.megatrex4.ukrainian_dlight.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class UkrainianDeligthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
    }
}
