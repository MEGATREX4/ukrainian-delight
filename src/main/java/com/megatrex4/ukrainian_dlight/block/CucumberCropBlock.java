package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class CucumberCropBlock extends CropBlock {
    public static final IntProperty AGE = IntProperty.of("age", 0, 4); // 0–4, 2–4 використовують 2 блоки
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

    public CucumberCropBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ItemsRegistry.CUCUMBER_SEEDS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        int age = state.get(AGE);
        if (age >= 2 && world.getBlockState(pos.down()).isOf(this) && world.getBlockState(pos.down()).get(AGE) == age) {
            return true; // верхній блок (2–4) існує лише над відповідним нижнім
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = getAge(state);

        // Верхній блок — не росте сам
        if (age >= 2 && world.getBlockState(pos.down()).isOf(this)) {
            return;
        }

        if (world.getBaseLightLevel(pos, 0) >= 9 && age < 4) {
            float chance = getAvailableMoisture(this, world, pos);
            if (random.nextInt((int) (25.0F / chance) + 1) == 0) {
                int nextAge = age + 1;
                world.setBlockState(pos, this.withAge(nextAge), 2);

                if (nextAge >= 2) {
                    BlockPos above = pos.up();
                    BlockState aboveState = world.getBlockState(above);
                    if (aboveState.isAir() || (aboveState.isOf(this) && aboveState.get(AGE) != nextAge)) {
                        world.setBlockState(above, this.withAge(nextAge), 2);
                    }
                }
            }
        }
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int age = getAge(state);
        if (age >= 2 && world.getBlockState(pos.down()).isOf(this)) return; // верхній не росте

        int growth = getGrowthAmount(world);
        int nextAge = Math.min(age + growth, 4);

        world.setBlockState(pos, this.withAge(nextAge), 2);

        if (nextAge >= 2) {
            BlockPos above = pos.up();
            BlockState aboveState = world.getBlockState(above);
            if (aboveState.isAir() || (aboveState.isOf(this) && aboveState.get(AGE) != nextAge)) {
                world.setBlockState(above, this.withAge(nextAge), 2);
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        int age = state.get(AGE);
        BlockPos below = pos.down();

        if (age >= 2 && world.getBlockState(below).isOf(this)) {
            world.setBlockState(below, this.withAge(1), 2); // якщо це верхній блок — знизити нижній
        }

        super.onBreak(world, pos, state, player);
    }
}
