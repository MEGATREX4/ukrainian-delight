package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.block.entity.ModBlockEntities;
import com.megatrex4.ukrainian_dlight.screen.renderer.FluidStackRenderer;
import com.megatrex4.ukrainian_dlight.util.FluidStack;

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
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewingKegBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int WATER_SLOT = 7;
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;


    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 16, 15);
    public static final DirectionProperty FACING;

    public BrewingKegBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) this.getStateManager().getDefaultState()).with(FACING, Direction.NORTH));
    }


    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
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
    @Environment(EnvType.CLIENT)
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
            if (tag.contains("TankContent", NbtElement.COMPOUND_TYPE)) {
                NbtCompound tankContent = tag.getCompound("TankContent");

                // Create a FluidStack from NBT data
                FluidStack fluidStack = createFluidStackFromNbt(tankContent); // Replace with your actual method

                // Render fluid tooltips using your FluidStackRenderer
                FluidStackRenderer fluidRenderer = new FluidStackRenderer();
                List<Text> fluidTooltip = fluidRenderer.getItemTooltip(fluidStack, TooltipContext.Default.BASIC);

                // Add the fluid-related tooltips to the main tooltip list
                tooltip.addAll(fluidTooltip);
            }
        } else {
            tooltip.add(UkrainianDelight.i18n("tooltip.empty").formatted(Formatting.GRAY));
        }

        // Remove the last "block.minecraft.empty" tooltip if present
        tooltip.removeIf(text -> text.getString().equals("block.minecraft.empty"));
    }

    // Example method to create FluidStack from NBT data
    private FluidStack createFluidStackFromNbt(NbtCompound tag) {
        FluidVariant fluidVariant = FluidVariant.fromNbt(tag.getCompound("Variant"));
        long amount = tag.getLong("Amount");
        return new FluidStack(fluidVariant, amount);
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
                NbtCompound tag = new NbtCompound();
                brewingKegEntity.writeNbt(tag);
                itemStack.setSubNbt("BlockEntityTag", tag);

                // Save DisplaySlot to NBT
                NbtCompound displaySlotTag = new NbtCompound();
                brewingKegEntity.getStack(DRINKS_DISPLAY_SLOT).writeNbt(displaySlotTag);
                itemStack.setSubNbt("DisplaySlot", displaySlotTag);

                // Spawn the item entity with the BlockEntityTag and DisplaySlot NBT
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                world.spawnEntity(itemEntity);
            }
            world.removeBlockEntity(pos); // Remove the block entity from the world
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public static void spawnParticles(World world, BlockPos pos, BlockState state) {
        Random random = world.random;
        if (world != null) {
            if (random.nextFloat() < .1f) {
                double baseX = pos.getX() + .5d + (random.nextDouble() * .4d - .2d);
                double baseY = pos.getY() + 1.1;
                double baseZ = pos.getZ() + .5d + (random.nextDouble() * .4d - .2d);
                // Debug particle spawn
                // System.out.println("Spawning steam");
                double motionY = random.nextBoolean() ? .015d : .005d;
                world.addParticle(ParticleTypes.SMOKE, baseX, baseY, baseZ, .0d, motionY, .0d);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getStackInHand(hand);
        if (!world.isClient() && world.getBlockEntity(pos) instanceof BrewingKegBlockEntity brewingKegBlockEntity) {
            ItemStack serving = brewingKegBlockEntity.useHeldItemOnDrink(heldStack);

            if (serving != ItemStack.EMPTY) {
                if (!player.getInventory().insertStack(serving)) {
                    player.dropItem(serving, false);
                }
                world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.f, 1.f);
                return ActionResult.SUCCESS;
            }

            if (heldStack.isOf(Items.WATER_BUCKET)) {
                long waterAmount = brewingKegBlockEntity.getWaterAmount();
                long waterCapacity = brewingKegBlockEntity.getWaterCapacity();
                if (waterAmount + 1000 <= waterCapacity) {
                    brewingKegBlockEntity.addWater(1000);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                    }
                    return ActionResult.SUCCESS;
                }
            } else {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    // Open the screen handler and set the container if needed
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    private boolean giveItemToPlayerOrDrop(World world, PlayerEntity player, ItemStack itemStack) {
        if (!player.getInventory().insertStack(itemStack)) {
            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), itemStack);
            world.spawnEntity(itemEntity);
            return false;
        }
        return true;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.BREWING_KEG_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> {
            blockEntity.tick(world1, pos, state1, (BrewingKegBlockEntity) blockEntity);
            BrewingKegBlock.spawnParticles(world1, pos, state1);
        });
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BrewingKegBlockEntity) {
            return ((BrewingKegBlockEntity) blockEntity).calculateComparatorOutput();
        }
        return super.getComparatorOutput(state, world, pos);
    }
}
