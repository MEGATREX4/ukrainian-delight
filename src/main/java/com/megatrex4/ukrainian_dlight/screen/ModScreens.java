package com.megatrex4.ukrainian_dlight.screen;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ModScreens {
    public static void registerScreens() {
        ScreenRegistry.register(ModScreenHandlers.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
    }
}
