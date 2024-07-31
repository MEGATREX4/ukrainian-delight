package com.megatrex4.ukrainian_dlight.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDirectory(), "ukrainian_delight.json");

    public static int brewingKegCapacity = 20; // default capacity

    public static int getBrewingKegCapacity() {
        return (int) (FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * brewingKegCapacity);
    }

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            saveConfig();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonObject configJson = GSON.fromJson(reader, JsonObject.class);
            brewingKegCapacity = configJson.get("brewingKegCapacity").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        JsonObject configJson = new JsonObject();
        configJson.addProperty("brewingKegCapacity", brewingKegCapacity);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
