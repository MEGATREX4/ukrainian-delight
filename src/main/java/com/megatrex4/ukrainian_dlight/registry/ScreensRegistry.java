package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class ScreensRegistry {
    public static void registerScreens() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ScreenRegistry.register(ScreenHandlersRegistry.BREWING_KEG_SCREEN_HANDLER, BrewingKegScreen::new);
        }
    }

}
