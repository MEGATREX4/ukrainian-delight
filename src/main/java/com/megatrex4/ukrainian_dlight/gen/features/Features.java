package com.megatrex4.ukrainian_dlight.gen.features;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.gen.features.features.SaltCavesFeature;
import com.megatrex4.ukrainian_dlight.gen.features.features.config.SaltCavesFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class Features {

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return (F) Registry.register(Registries.FEATURE, UkrainianDelight.id(id), feature);
    }

    public final static Feature<SaltCavesFeatureConfig> SALT_CAVES_FEATURE;

    static {
        SALT_CAVES_FEATURE = register("salt_caves_feature", new SaltCavesFeature(SaltCavesFeatureConfig.CODEC));
    }

    public static void init() {
        new Features();
    }

}
