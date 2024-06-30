package com.megatrex4.ukrainian_dlight.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CustomSaltBlock extends Block {
    private static final int SCHEDULED_TICK_DELAY = 20;

    public CustomSaltBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof SlimeEntity) {
            entity.damage(world.getDamageSources().generic(), 2.0F);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Custom scheduled tick behavior, if needed
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, SCHEDULED_TICK_DELAY);
    }
}
