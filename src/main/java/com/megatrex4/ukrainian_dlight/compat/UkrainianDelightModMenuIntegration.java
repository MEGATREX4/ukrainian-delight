package com.megatrex4.ukrainian_dlight.compat;

import com.megatrex4.ukrainian_dlight.screen.UkrainianDelightConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class UkrainianDelightModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return UkrainianDelightConfigScreen::getScreen;
    }
}

