package com.megatrex4.ukrainian_dlight.features.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record SaltCavesFeatureConfig(IntProvider size, IntProvider amount, IntProvider spread, BlockState innerState,
                                  BlockState outerState) implements FeatureConfig
{
    public static final Codec<SaltCavesFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(SaltCavesFeatureConfig::size),
            IntProvider.VALUE_CODEC.fieldOf("amount").forGetter(SaltCavesFeatureConfig::amount),
            IntProvider.VALUE_CODEC.fieldOf("spread").forGetter(SaltCavesFeatureConfig::spread),
            BlockState.CODEC.fieldOf("inner_state").forGetter(SaltCavesFeatureConfig::innerState),
            BlockState.CODEC.fieldOf("outer_state").forGetter(SaltCavesFeatureConfig::outerState)
    ).apply(instance, instance.stable(SaltCavesFeatureConfig::new)));
}
