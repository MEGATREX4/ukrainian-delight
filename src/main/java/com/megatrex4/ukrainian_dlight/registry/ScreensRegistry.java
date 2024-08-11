package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreen;
import com.megatrex4.ukrainian_dlight.screen.PysankersTableScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ScreensRegistry {
    public static void registerScreens() {
        ScreenRegistry.register(ScreenHandlersRegistry.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
        ScreenRegistry.register(ScreenHandlersRegistry.PYSANKERS_TABLE_SCREEN_HANDLER, PysankersTableScreen::new);
    }
}
