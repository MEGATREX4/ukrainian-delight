package com.megatrex4.ukrainian_dlight.block.custom;

import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.block.entity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BrewingKegBlock extends BlockWithEntity implements BlockEntityProvider {

    //adds 6 input ingredients slot
    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    //adds container(like bottle of vial) input
    public static final int CONTAINER_SLOT = 6;
    //adds 1 input slot for water
    public static final int WATER_SLOT = 7;
    //adds 1 output slot and display slot
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public static final int REQUIRE_CONTAINER = 10;


    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);
    public static final DirectionProperty FACING;

    public BrewingKegBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.getStateManager().getDefaultState()).with(FACING, Direction.NORTH))));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(new Property[]{FACING});
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        FluidState fluidState = world.getFluidState(blockPos);
        return (BlockState)((BlockState)((BlockState)this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite())));
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BrewingKegBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BrewingKegBlockEntity) {
                BrewingKegBlockEntity brewingKegEntity = (BrewingKegBlockEntity) blockEntity;
                // Create an item stack with the block's item
                ItemStack itemStack = new ItemStack(this.asItem());
                // Save BlockEntity data to NBT
                NbtCompound tag = brewingKegEntity.writeToNbtPublic(new NbtCompound()); // Use the public method to get NBT
                itemStack.setSubNbt("BlockEntityTag", tag);

                // Save DRINKS_DISPLAY_SLOT to NBT and remove from block entity
                NbtCompound displaySlotTag = new NbtCompound();
                brewingKegEntity.getStack(DRINKS_DISPLAY_SLOT).writeNbt(displaySlotTag);
                itemStack.setSubNbt("DisplaySlot", displaySlotTag);

                // Save REQUARE_CONTAINER to NBT and remove from block entity
                NbtCompound requireContainerTag = new NbtCompound();
                brewingKegEntity.getStack(REQUIRE_CONTAINER).writeNbt(requireContainerTag);
                itemStack.setSubNbt("requireContainer", requireContainerTag);

                // Drop items from INGREDIENT_SLOTS (0-7) and WATER_SLOT (9)
                for (int slot : BrewingKegBlockEntity.INGREDIENT_SLOTS) {
                    if (slot != BrewingKegBlockEntity.WATER_SLOT) { // Skip WATER_SLOT
                        dropSlotContents(world, pos, brewingKegEntity, slot);
                    }
                }
                dropSlotContents(world, pos, brewingKegEntity, BrewingKegBlockEntity.WATER_SLOT);

                // Drop the OUTPUT_SLOT
                dropSlotContents(world, pos, brewingKegEntity, BrewingKegBlockEntity.OUTPUT_SLOT);

                // Spawn the item entity with the BlockEntityTag and DisplaySlot NBT
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                world.spawnEntity(itemEntity);
            }
            world.removeBlockEntity(pos); // Remove the block entity from the world
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


    private void dropSlotContents(World world, BlockPos pos, BrewingKegBlockEntity brewingKegEntity, int slot) {
        ItemStack stack = brewingKegEntity.getStack(slot);
        if (!stack.isEmpty()) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            brewingKegEntity.setStack(slot, ItemStack.EMPTY);
        }
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, net.minecraft.util.hit.BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = (BrewingKegBlockEntity) world.getBlockEntity(pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.BREWING_KEG_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }

}
