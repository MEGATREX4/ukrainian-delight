package com.megatrex4.ukrainian_dlight.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BottleBlock extends Block {
    public static final IntProperty BOTTLES = IntProperty.of("bottles", 1, 6);
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    private static final Map<Integer, VoxelShape> SHAPES = new HashMap<>();

    static {
        // Define shapes for different number of bottles here
        SHAPES.put(1, Block.createCuboidShape(6, 0, 6, 10, 15, 10));
        SHAPES.put(2, Block.createCuboidShape(1, 0, 3, 14, 15, 12));
        SHAPES.put(3, Block.createCuboidShape(1, 0, 1, 14, 15, 14));
        SHAPES.put(4, Block.createCuboidShape(1, 0, 1, 14, 15, 14));
        SHAPES.put(5, Block.createCuboidShape(0, 0, 0, 14, 15, 14));
        SHAPES.put(6, Block.createCuboidShape(0, 0, 0, 14, 15, 14));
    }

    public BottleBlock() {
        super(FabricBlockSettings.copyOf(Blocks.GLASS).strength(0.2F).nonOpaque().sounds(BlockSoundGroup.GLASS));
        this.setDefaultState(this.stateManager.getDefaultState().with(BOTTLES, 1).with(FACING, Direction.NORTH));
        setRenderLayer();
    }

    private void setRenderLayer() {
        BlockRenderLayerMap.INSTANCE.putBlock(this, RenderLayer.getCutout());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTLES, FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(BOTTLES));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(BOTTLES, 1).with(FACING, facing);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int currentBottles = state.get(BOTTLES);
        ItemStack heldItem = player.getStackInHand(hand);

        // Check if the player is using an empty hand to remove a bottle
        if (heldItem.isEmpty() && currentBottles > 0) {
            if (currentBottles == 1) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState()); // Break the block when the last bottle is removed
            } else {
                world.setBlockState(pos, state.with(BOTTLES, currentBottles - 1)); // Reduce bottles by 1
            }
            dropBottleItem(world, pos, 1); // Drop 1 bottle
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_STEP, SoundCategory.BLOCKS, 2f, 0.7f); // Glass step sound
            return ActionResult.SUCCESS;
        }

        // Check if the player is holding a bottle block item to add a bottle and it matches the current block
        if (!heldItem.isEmpty() && currentBottles < 6 && heldItem.getItem() == this.asItem()) {
            world.setBlockState(pos, state.with(BOTTLES, currentBottles + 1));
            if (!player.isCreative()) {
                heldItem.decrement(1);
            }
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); // Glass place sound
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            super.onStateReplaced(state, world, pos, newState, moved);
            if (state.get(BOTTLES) > 0 && (newState.isAir() || newState.getBlock() != this)) {
                dropBottleItem(world, pos, state.get(BOTTLES));
            }
        }
    }

    private void dropBottleItem(World world, BlockPos pos, int count) {
        if (!world.isClient) {
            ItemStack bottleItemStack = new ItemStack(this.asItem(), count);
            Block.dropStack(world, pos, bottleItemStack);
        }
    }
}
