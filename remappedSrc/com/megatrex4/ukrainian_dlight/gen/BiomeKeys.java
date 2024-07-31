package com.megatrex4.ukrainian_dlight.gen;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public class BiomeKeys {

    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(RegistryKeys.BIOME, UkrainianDelight.id(name));
    }

    public static final RegistryKey<Biome> SALT_CAVES = register("salt_caves");

}
