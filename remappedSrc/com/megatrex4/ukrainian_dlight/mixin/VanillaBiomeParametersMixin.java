package com.megatrex4.ukrainian_dlight.mixin;

import com.megatrex4.ukrainian_dlight.gen.CaveBiomes;
import com.megatrex4.ukrainian_dlight.gen.CaveBiomes.CaveBiome;
import com.megatrex4.ukrainian_dlight.util.VanillaBiomeParametersHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public class VanillaBiomeParametersMixin {
    @Inject(at = @At("RETURN"), method = "writeCaveBiomes")
    private void init(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, CallbackInfo info) {
        for (CaveBiome b : CaveBiomes.DEFAULT_CAVE_BIOMES) {
            VanillaBiomeParametersHelper.writeCaveBiomeParameters(parameters, b.temperature, b.humidity, b.continentalness, b.erosion, b.depth, b.weirdness, b.offset, b.biome);
        }
    }
}
