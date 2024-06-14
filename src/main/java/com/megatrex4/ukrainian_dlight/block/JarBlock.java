package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.item.JarsItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class JarBlock extends Block {
    public static final IntProperty JARS = IntProperty.of("jars", 1, 4);
    private static final VoxelShape ONE_JAR_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 8, 10);
    private static final VoxelShape TWO_JARS_SHAPE = VoxelShapes.union(ONE_JAR_SHAPE, Block.createCuboidShape(1, 0, 3, 14, 8, 12));
    private static final VoxelShape THREE_JARS_SHAPE = VoxelShapes.union(TWO_JARS_SHAPE, Block.createCuboidShape(1, 0, 1, 15, 8, 14));
    private static final VoxelShape FOUR_JARS_SHAPE = VoxelShapes.union(THREE_JARS_SHAPE, Block.createCuboidShape(1, 0, 1, 15, 8, 15));

    public JarBlock() {
        super(FabricBlockSettings.copyOf(Blocks.GLASS).strength(0.3F).nonOpaque().sounds(BlockSoundGroup.GLASS));
        this.setDefaultState(this.stateManager.getDefaultState().with(JARS, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(JARS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    private VoxelShape getShape(BlockState state) {
        switch (state.get(JARS)) {
            case 1:
            default:
                return ONE_JAR_SHAPE;
            case 2:
                return TWO_JARS_SHAPE;
            case 3:
                return THREE_JARS_SHAPE;
            case 4:
                return FOUR_JARS_SHAPE;
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(JARS, 1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int currentJars = state.get(JARS);
        ItemStack heldItem = player.getStackInHand(hand);

        // Check if the player is using an empty hand to remove a jar
        if (heldItem.isEmpty()) {
            if (currentJars > 1) {
                world.setBlockState(pos, state.with(JARS, currentJars - 1));
                dropJarItem(world, pos, state.getBlock());
                return ActionResult.SUCCESS;
            } else if (currentJars == 1) {
                world.breakBlock(pos, false);
                dropJarItem(world, pos, state.getBlock());
                return ActionResult.SUCCESS;
            }
        }

        // Check if the player is holding a jar item to add a jar
        if (currentJars < 4 && isValidJarItem(heldItem.getItem(), state.getBlock())) {
            world.setBlockState(pos, state.with(JARS, currentJars + 1));
            if (!player.isCreative()) {
                heldItem.decrement(1);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private boolean isValidJarItem(Item item, Block block) {
        if (block == ModBlocks.JAR_BLOCK && item == JarsItems.JAR) {
            return true;
        } else if (block == ModBlocks.APPLE_JAM_JAR_BLOCK && item == JarsItems.APPLE_JAM) {
            return true;
        } else if (block == ModBlocks.TINNED_TOMATOES_BLOCK && item == JarsItems.TINNED_TOMATOES) {
            return true;
        }
        return false;
    }

    private void dropJarItem(World world, BlockPos pos, Block block) {
        if (!world.isClient) {
            Vec3d dropPos = Vec3d.ofCenter(pos);
            ItemStack jarItemStack = getJarItemStack(block);
            Block.dropStack(world, pos, jarItemStack);
        }
    }

    private ItemStack getJarItemStack(Block block) {
        if (block == ModBlocks.APPLE_JAM_JAR_BLOCK) {
            return new ItemStack(JarsItems.APPLE_JAM);
        } else if (block == ModBlocks.TINNED_TOMATOES_BLOCK) {
            return new ItemStack(JarsItems.TINNED_TOMATOES);
        }
        return new ItemStack(JarsItems.JAR);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return getJarItemStack(state.getBlock());
    }
}
