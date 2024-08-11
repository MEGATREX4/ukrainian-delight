package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreen;
import com.megatrex4.ukrainian_dlight.registry.ScreenHandlersRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class UkrainianDeligthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlersRegistry.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
    }
}
