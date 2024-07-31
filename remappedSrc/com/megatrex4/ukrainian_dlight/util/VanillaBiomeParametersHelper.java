package com.megatrex4.ukrainian_dlight.util;

import com.megatrex4.ukrainian_dlight.gen.BiomeKeys;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class VanillaBiomeParametersHelper {
    public static void writeCaveBiomeParameters(
            java.util.function.Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters,
            MultiNoiseUtil.ParameterRange temperature,
            MultiNoiseUtil.ParameterRange humidity,
            MultiNoiseUtil.ParameterRange continentalness,
            MultiNoiseUtil.ParameterRange erosion,
            MultiNoiseUtil.ParameterRange depth,
            MultiNoiseUtil.ParameterRange weirdness,
            float offset,
            RegistryKey<Biome> biome) {

        parameters.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, depth, weirdness, offset),
                biome
        ));
    }
}
