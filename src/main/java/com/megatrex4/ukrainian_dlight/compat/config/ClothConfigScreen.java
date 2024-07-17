package com.megatrex4.ukrainian_dlight.compat.config;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;

public class ClothConfigScreen {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(UkrainianDelight.i18n("title.config"));

        ConfigCategory general = builder.getOrCreateCategory(UkrainianDelight.i18n("category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startIntField(UkrainianDelight.i18n("option.brewing_keg_capacity"), ModConfig.brewingKegCapacity)
                .setDefaultValue(20000)
                .setSaveConsumer(newValue -> ModConfig.brewingKegCapacity = newValue)
                .build());

        builder.setSavingRunnable(ModConfig::saveConfig);

        return builder.build();
    }
}
