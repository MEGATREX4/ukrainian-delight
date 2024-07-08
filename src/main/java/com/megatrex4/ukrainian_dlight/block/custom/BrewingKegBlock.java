package com.megatrex4.ukrainian_dlight.block.custom;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.block.entity.ModBlockEntities;
import com.megatrex4.ukrainian_dlight.util.CompoundTagUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class BrewingKegBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int REQUIRE_CONTAINER = 7;
    public static final int WATER_SLOT = 8;
    public static final int DRINKS_DISPLAY_SLOT = 9;
    public static final int OUTPUT_SLOT = 10;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;


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
    @Environment(value = EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        NbtCompound tag = stack.getSubNbt("BlockEntityTag");
        if (tag != null) {
            // Display the item in the display slot
            if (tag.contains("DisplaySlot", NbtElement.COMPOUND_TYPE)) {
                NbtCompound displaySlotTag = tag.getCompound("DisplaySlot");
                ItemStack drink = ItemStack.fromNbt(displaySlotTag);
                if (!drink.isEmpty()) {
                    MutableText servingsOf = drink.getCount() == 1
                            ? UkrainianDelight.i18n("tooltip.single_serving")
                            : UkrainianDelight.i18n("tooltip.many_servings", drink.getCount());
                    tooltip.add(servingsOf.formatted(Formatting.GRAY));
                    MutableText drinkName = drink.getName().copy();
                    tooltip.add(drinkName.formatted(drink.getRarity().formatting));
                } else {
                    tooltip.add(UkrainianDelight.i18n("tooltip.empty").formatted(Formatting.GRAY));
                }
            } else {
                tooltip.add(UkrainianDelight.i18n("tooltip.empty").formatted(Formatting.GRAY));
            }

            // Display the fluid amount and capacity only if fluid_amount > 0
            if (tag.contains("fluid_amount", NbtElement.LONG_TYPE)) {
                long fluidAmount = tag.getLong("fluid_amount");
                if (fluidAmount > 0) { // Only show fluid-related tooltips if fluid_amount is greater than 0
                    long capacity = tag.contains("capacity", NbtElement.LONG_TYPE) ? tag.getLong("capacity") : 0L;

                    // Display the fluid name
                    if (tag.contains("fluid_variant", NbtElement.COMPOUND_TYPE)) {
                        NbtCompound fluidVariantTag = tag.getCompound("fluid_variant");
                        FluidVariant fluidVariant = FluidVariant.fromNbt(fluidVariantTag);
                        String fluidKey = Registries.FLUID.getId(fluidVariant.getFluid()).toTranslationKey();
                        MutableText fluidName = Text.translatable("block." + fluidKey);

                        // Format the tooltip with fluid name, amount, and capacity
                        MutableText fluidAmountText = UkrainianDelight.i18n("tooltip.fluid_amount", fluidName.getString(), fluidAmount, capacity);
                        tooltip.add(fluidAmountText.formatted(Formatting.GRAY));
                    } else {
                        // If fluid_variant is not present, display the amount without fluid name
                        MutableText fluidAmountText = UkrainianDelight.i18n("tooltip.fluid_amount_no_fluid", fluidAmount, capacity);
                        tooltip.add(fluidAmountText.formatted(Formatting.GRAY));
                    }
                }
            }
        } else {
            tooltip.add(UkrainianDelight.i18n("tooltip.empty").formatted(Formatting.GRAY));
        }

        // Remove the last "block.minecraft.empty" tooltip if present
        Iterator<Text> iterator = tooltip.iterator();
        while (iterator.hasNext()) {
            Text text = iterator.next();
            if (text.getString().equals("block.minecraft.empty")) {
                iterator.remove();
            }
        }
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
