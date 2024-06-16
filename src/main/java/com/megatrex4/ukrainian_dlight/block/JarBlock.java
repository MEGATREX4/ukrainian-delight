package com.megatrex4.ukrainian_dlight.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.megatrex4.ukrainian_dlight.UkrainianDelight.GLASS_DAMAGE;

public class JarBlock extends Block {
    public static final IntProperty JARS = IntProperty.of("jars", 1, 4);
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    private static final Map<Integer, VoxelShape> SHAPES = new HashMap<>();
    private static final ThreadLocal<Boolean> isRemovingJar = ThreadLocal.withInitial(() -> false);



    static {
        SHAPES.put(1, Block.createCuboidShape(6, 0, 6, 10, 8, 10));
        SHAPES.put(2, Block.createCuboidShape(1, 0, 3, 14, 8, 12));
        SHAPES.put(3, Block.createCuboidShape(1, 0, 1, 15, 8, 14));
        SHAPES.put(4, Block.createCuboidShape(1, 0, 1, 15, 8, 15));
    }

    public JarBlock() {
        super(FabricBlockSettings.copyOf(Blocks.GLASS).strength(0.2F).nonOpaque().sounds(BlockSoundGroup.GLASS));
        this.setDefaultState(this.stateManager.getDefaultState().with(JARS, 1).with(FACING, Direction.NORTH)); // Start with 1 jar facing north
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(JARS, FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(JARS));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        return ctx.getPlayer().isSneaking() ? this.getDefaultState().with(JARS, 1).with(FACING, facing) : null; // Set the facing direction based on player placement
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int currentJars = state.get(JARS);
        ItemStack heldItem = player.getStackInHand(hand);

        // Check if the player is using an empty hand to remove a jar
        if (heldItem.isEmpty() && currentJars > 0) {
            if (currentJars == 1) {
                isRemovingJar.set(true); // Set flag to indicate the jar is being removed
                world.setBlockState(pos, Blocks.AIR.getDefaultState()); // Break the block when the last jar is removed
                isRemovingJar.set(false); // Reset flag after removing
            } else {
                world.setBlockState(pos, state.with(JARS, currentJars - 1)); // Reduce jars by 1
            }
            dropJarItem(world, pos, 1); // Drop 1 jar
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_STEP, SoundCategory.BLOCKS, 2f, 0.7f); // Glass step sound
            return ActionResult.SUCCESS;
        }

        // Check if the player is holding a jar block item to add a jar and it matches the current block
        if (!heldItem.isEmpty() && currentJars < 4 && heldItem.getItem() == this.asItem()) {
            world.setBlockState(pos, state.with(JARS, currentJars + 1));
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
            if (!isRemovingJar.get() && state.get(JARS) > 0 && (newState.isAir() || newState.getBlock() != this)) {
                dropJarItem(world, pos, state.get(JARS));
            }
        }
    }

    private void dropJarItem(World world, BlockPos pos, int count) {
        if (!world.isClient) {
            ItemStack jarItemStack = new ItemStack(this.asItem(), count);
            Block.dropStack(world, pos, jarItemStack);
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        this.tryBreakJar(world, state, pos, entity, 0.3); // 30% chance
        super.onLandedUpon(world, state, pos, entity, fallDistance);

    }

    private void tryBreakJar(World world, BlockState state, BlockPos pos, Entity entity, double breakChance) {
        if (this.breaksJar(world, entity)) {
            if (!world.isClient && world.random.nextDouble() < breakChance) {
                this.breakJar(world, pos, state);
                entity.damage(world.getDamageSources().create(GLASS_DAMAGE), 2.0F);
            }
        }
    }


    private void breakJar(World world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
        int currentJars = state.get(JARS);
        if (currentJars <= 1) {
            isRemovingJar.set(true); // Set flag to avoid dropping items
            world.breakBlock(pos, false);
            isRemovingJar.set(false); // Reset flag after breaking
        } else {
            world.setBlockState(pos, state.with(JARS, currentJars - 1), 2);
            world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GLASS.getDefaultState()), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.0, 0.0); // Glass break particles
        }
    }

    private boolean breaksJar(World world, Entity entity) {
        return entity instanceof PlayerEntity;
    }
}
