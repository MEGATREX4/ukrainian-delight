package com.megatrex4.ukrainian_dlight.block.custom;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.entity.BrewingKegBlockEntity;
import com.megatrex4.ukrainian_dlight.block.entity.ModBlockEntities;
import com.megatrex4.ukrainian_dlight.util.CompoundTagUtils;
import com.nhoryzon.mc.farmersdelight.registry.ParticleTypesRegistry;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
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
            if (tag.contains("TankContent", NbtElement.COMPOUND_TYPE)) {
                NbtCompound tankContent = tag.getCompound("TankContent");
                long fluidAmount = tankContent.getLong("Amount");
                if (fluidAmount > 0) { // Only show fluid-related tooltips if fluid_amount is greater than 0
                    long capacity = tankContent.getLong("Capacity");

                    // Display the fluid name
                    NbtCompound fluidVariantTag = tankContent.getCompound("Variant");
                    FluidVariant fluidVariant = FluidVariant.fromNbt(fluidVariantTag);
                    String fluidKey = Registries.FLUID.getId(fluidVariant.getFluid()).toTranslationKey();
                    MutableText fluidName = Text.translatable("block." + fluidKey);

                    // Format the tooltip with fluid name, amount, and capacity
                    MutableText fluidAmountText = UkrainianDelight.i18n("tooltip.fluid_amount", fluidName.getString(), fluidAmount, capacity);
                    tooltip.add(fluidAmountText.formatted(Formatting.GRAY));
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
                world.addParticle(ParticleTypesRegistry.STEAM.get(), baseX, baseY, baseZ, .0d, motionY, .0d);
            }
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ItemStack heldItem = player.getStackInHand(hand);
            BrewingKegBlockEntity blockEntity = (BrewingKegBlockEntity) world.getBlockEntity(pos);

            if (blockEntity == null) {
                return ActionResult.PASS;
            }

            if (hand == Hand.MAIN_HAND) {
                if (heldItem.isOf(Items.WATER_BUCKET)) {
                    long waterAmount = blockEntity.getWaterAmount();
                    long waterCapacity = blockEntity.getWaterCapacity();

                    if (waterAmount + 1000 <= waterCapacity) {
                        blockEntity.addWater(1000);
                        if (!world.isClient) {
                            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                        if (!player.isCreative()) {
                            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        }
                        return ActionResult.SUCCESS;
                    }
                } else {
                    ItemStack requiredContainer = blockEntity.getStack(REQUIRE_CONTAINER);

                    if (heldItem.isOf(requiredContainer.getItem())) {
                        ItemStack displayItem = blockEntity.getStack(DRINKS_DISPLAY_SLOT);

                        if (!displayItem.isEmpty() && displayItem.getCount() > 0) {

                            ItemStack filledContainer = displayItem.copy(); // Use the display item as the filled container
                            filledContainer.setCount(1);

                            displayItem.decrement(1);
                            heldItem.decrement(1);

                            // Attempt to give the filled container to the player
                            if (!player.getInventory().insertStack(filledContainer)) {
                                // If player inventory is full, spawn it as an entity at player's position
                                ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), filledContainer);
                                world.spawnEntity(itemEntity);
                            }

                            blockEntity.markDirty();
                            blockEntity.sendFluidPacket();
                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }

            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

//    private ActionResult handleWaterBucket(World world, BlockPos pos, PlayerEntity player, Hand hand, BrewingKegBlockEntity blockEntity) {
//        ItemStack heldItem = player.getStackInHand(hand);
//        long waterAmount = blockEntity.getWaterAmount();
//        long waterCapacity = blockEntity.getWaterCapacity();
//
//        if (waterAmount + 1000 <= waterCapacity) {
//            blockEntity.addWater(1000);
//            if (!world.isClient) {
//                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
//            }
//            if (!player.isCreative()) {
//                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
//            }
//            return ActionResult.SUCCESS;
//        }
//        return ActionResult.FAIL;
//    }
//
//    private ActionResult handleContainerFill(World world, BlockPos pos, PlayerEntity player, Hand hand, BrewingKegBlockEntity blockEntity) {
//        ItemStack heldItem = player.getStackInHand(hand);
//        ItemStack requiredContainer = blockEntity.getStack(REQUIRE_CONTAINER);
//
//        if (heldItem.isOf(requiredContainer.getItem())) {
//            ItemStack displayItem = blockEntity.getStack(DRINKS_DISPLAY_SLOT);
//
//            if (!displayItem.isEmpty() && displayItem.getCount() > 0) {
//                ItemStack filledContainer = displayItem.copy();
//                filledContainer.setCount(1);
//
//                displayItem.decrement(1);
//                heldItem.decrement(1);
//
//                if (!giveItemToPlayerOrDrop(world, player, filledContainer)) {
//                    return ActionResult.FAIL;
//                }
//
//                blockEntity.markDirty();
//                blockEntity.sendFluidPacket();
//                return ActionResult.SUCCESS;
//            }
//        }
//        return ActionResult.FAIL;
//    }

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
            blockEntity.tick(world1, pos, state1);
            BrewingKegBlock.spawnParticles(world1, pos, state1);
        });
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }

}
