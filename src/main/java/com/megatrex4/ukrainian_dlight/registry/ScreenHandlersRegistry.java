package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlersRegistry {
    public static final ScreenHandlerType<BrewingKegScreenHandler> BREWING_KEG_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(UkrainianDelight.MOD_ID, "brewing_keg_screen_handler"),
                    new ExtendedScreenHandlerType<>(BrewingKegScreenHandler::new));

    public static void registerModScreenHandlers() {
        UkrainianDelight.LOGGER.info("Registering Mod Screen Handlers for " + UkrainianDelight.MOD_ID);
        // If there are more screen handlers, register them here
    }
}
