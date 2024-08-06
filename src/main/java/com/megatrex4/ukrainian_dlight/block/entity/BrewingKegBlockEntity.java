package com.megatrex4.ukrainian_dlight.block.entity;

import com.megatrex4.ukrainian_dlight.block.custom.BrewingKegBlock;
import com.megatrex4.ukrainian_dlight.block.entity.inventory.ImplementedInventory;
import com.megatrex4.ukrainian_dlight.config.ModConfig;
import com.megatrex4.ukrainian_dlight.networking.ModMessages;
import com.megatrex4.ukrainian_dlight.recipe.BrewingRecipe;
import com.megatrex4.ukrainian_dlight.recipe.ModRecipes;
import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreenHandler;
import com.megatrex4.ukrainian_dlight.util.CompoundTagUtils;
import com.megatrex4.ukrainian_dlight.util.FluidStack;



import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BrewingKegBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public static final String TAG_KEY_BREWING_RECIPES_USED = "RecipesUsed";

    private long capacity;

    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int WATER_SLOT = 7;
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;


    // Define OUTPUT_SLOTS and ALL_SLOTS_EXCEPT_INGREDIENTS
    private static final int[] OUTPUT_SLOTS = {OUTPUT_SLOT};
    private static final int[] ALL_SLOTS_EXCEPT_INGREDIENTS = {CONTAINER_SLOT, WATER_SLOT, DRINKS_DISPLAY_SLOT, OUTPUT_SLOT};


    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> experienceTracker;
    private int progress;
    private int maxProgress = 200;  // Adjusted to match the brewing time in the JSON
    private Text customName;
    private ItemStack drinkContainer;


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }


    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertMbToDroplets(ModConfig.getBrewingKegCapacity()); // 20k mB
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if(!world.isClient()) {
                sendFluidPacket();
            }
        }
    };


    public BrewingKegBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREWING_KEG_BLOCK_ENTITY, pos, state);
        drinkContainer = ItemStack.EMPTY;
        experienceTracker = new Object2IntOpenHashMap<>();
        this.capacity = ModConfig.getBrewingKegCapacity();
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BrewingKegBlockEntity.this.progress;
                    case 1 -> BrewingKegBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrewingKegBlockEntity.this.progress = value;
                    case 1 -> BrewingKegBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public long getMaxWaterLevel() {
        return fluidStorage.getCapacity();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN ? OUTPUT_SLOTS : ALL_SLOTS_EXCEPT_INGREDIENTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if (slot == DRINKS_DISPLAY_SLOT || slot == OUTPUT_SLOT) {
            return false;
        }

        // Allow water buckets to be inserted into WATER_SLOT
        if (slot == WATER_SLOT && stack.getItem() == Items.WATER_BUCKET) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (slot == OUTPUT_SLOT) {
            return true;
        }
        if (slot == WATER_SLOT && stack.getItem() == Items.BUCKET) {
            return true;
        }
        return false;
    }


    public ItemStack getDrink() {
        return getStack(DRINKS_DISPLAY_SLOT);
    }


    public Text getName() {
        return customName != null ? customName : Text.translatable("gui.ukrainian_delight.brewing_keg");
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, inventory);
        tag.putInt("progress", progress);

        // Save DRINKS_DISPLAY_SLOT
        NbtCompound displaySlotTag = new NbtCompound();
        ItemStack displayStack = getStack(DRINKS_DISPLAY_SLOT);
        if (!displayStack.isEmpty()) {
            displayStack.writeNbt(displaySlotTag);
        }
        tag.put("DisplaySlot", displaySlotTag);

        tag.put(CompoundTagUtils.TAG_KEY_CONTAINER, drinkContainer.writeNbt(new NbtCompound()));
        System.out.println("BrewingKegBlockEntity: writeNbt drinkContainer: " + drinkContainer);
        writeInventoryNbt(tag);

        NbtCompound compoundRecipes = new NbtCompound();
        experienceTracker.forEach((identifier, craftedAmount) -> compoundRecipes.putInt(identifier.toString(), craftedAmount));
        tag.put(TAG_KEY_BREWING_RECIPES_USED, compoundRecipes);

        // Save water
        NbtCompound tankContent = new NbtCompound();
        NbtCompound variant = fluidStorage.variant.toNbt();
        tankContent.put("Variant", variant);
        tankContent.putLong("Capacity", fluidStorage.getCapacity());
        tankContent.putLong("Amount", fluidStorage.amount);
        tag.put("TankContent", tankContent);
    }



    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, inventory);
        progress = tag.getInt("progress");

        drinkContainer = ItemStack.fromNbt(tag.getCompound(CompoundTagUtils.TAG_KEY_CONTAINER));
        // debug log drink container
        System.out.println("BrewingKegBlockEntity: readNbt drinkContainer: " + drinkContainer);

        // Load DRINKS_DISPLAY_SLOT
        if (tag.contains("DisplaySlot", NbtType.COMPOUND)) {
            NbtCompound displaySlotTag = tag.getCompound("DisplaySlot");
            setStack(DRINKS_DISPLAY_SLOT, ItemStack.fromNbt(displaySlotTag));
        } else {
            setStack(DRINKS_DISPLAY_SLOT, ItemStack.EMPTY); // Ensure it's clear if no DisplaySlot is found
        }

        // Load water
        if (tag.contains("TankContent", NbtType.COMPOUND)) {
            NbtCompound tankContent = tag.getCompound("TankContent");
            fluidStorage.variant = FluidVariant.fromNbt(tankContent.getCompound("Variant"));
            fluidStorage.amount = tankContent.getLong("Amount");
        }

        NbtCompound compoundRecipes = tag.getCompound(TAG_KEY_BREWING_RECIPES_USED);
        for (String key : compoundRecipes.getKeys()) {
            experienceTracker.put(new Identifier(key), compoundRecipes.getInt(key));
        }
    }

    public NbtCompound writeDrink(NbtCompound tag) {
        if (getDrink().isEmpty()) {
            return tag;
        }

        if (customName != null) {
            tag.putString(CompoundTagUtils.TAG_KEY_CUSTOM_NAME, Text.Serializer.toJson(customName));
        }
        tag.put(CompoundTagUtils.TAG_KEY_CONTAINER, drinkContainer.writeNbt(new NbtCompound()));

        DefaultedList<ItemStack> drops = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.set(i, i == DRINKS_DISPLAY_SLOT ? getStack(i) : ItemStack.EMPTY);
        }
        tag.put(CompoundTagUtils.TAG_KEY_INVENTORY, Inventories.writeNbt(new NbtCompound(), drops));

        return tag;
    }




    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }



    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        sendFluidPacket();
        return new BrewingKegScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state, BrewingKegBlockEntity blockEntity) {
        boolean dirty = false;

        handleWaterBucket();
        boolean enoughFluid = hasEnoughFluid();

        if (blockEntity.hasInput() && enoughFluid) {
            Optional<BrewingRecipe> match = getCurrentRecipe();
            if (match.isPresent()) {
                BrewingRecipe recipe = match.get();
                if (blockEntity.canCook(recipe)) {
                    dirty = blockEntity.processBrewing(recipe);

                } else {
                    blockEntity.progress = 0;
                }
            }
        } else if (blockEntity.progress > 0) {
            blockEntity.progress = MathHelper.clamp(blockEntity.progress - 2, 0, blockEntity.maxProgress);
        }

        ItemStack drink = blockEntity.getDrink();
        if (!blockEntity.isEmpty()) {
            if (!blockEntity.doesDrinkHaveContainer(drink)) {
                blockEntity.moveDrinkToOutput();
                dirty = true;
            } else if (!blockEntity.getStack(CONTAINER_SLOT).isEmpty()) {
                blockEntity.useStoredContainersOnDrink();
                dirty = true;
            }
        }

        if (dirty) {
            markDirty();
        }
    }

    private boolean hasInput() {
        for (int i = 0; i < DRINKS_DISPLAY_SLOT; ++i) {
            if (!getStack(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }


    protected boolean canCook(Recipe<?> recipeIn) {
        if (hasInput() && recipeIn != null) {
            ItemStack recipeOutput = recipeIn.getOutput(world.getRegistryManager());
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack currentOutput = getStack(DRINKS_DISPLAY_SLOT);
                if (currentOutput.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsEqual(currentOutput, recipeOutput)) {
                    return false;
                } else if (currentOutput.getCount() + recipeOutput.getCount() <= getMaxCountPerStack()) {
                    return true;
                } else {
                    return currentOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }


    private boolean processBrewing(BrewingRecipe recipe) {
        if (world == null || recipe == null) return false;

        ++progress;
        maxProgress = recipe.getBrewingTime();
        if (progress < maxProgress) {
            return false;
        }

        progress = 0;
        drinkContainer = recipe.getContainer();
        ItemStack recipeOutput = recipe.getOutput(world.getRegistryManager());
        ItemStack currentOutput = getStack(DRINKS_DISPLAY_SLOT);
        if (currentOutput.isEmpty()) {
            setStack(DRINKS_DISPLAY_SLOT, recipeOutput.copy());
        } else if (currentOutput.getItem() == recipeOutput.getItem()) {
            currentOutput.increment(recipeOutput.getCount());
        }
        trackRecipeExperience(recipe);
        this.extractFluid(recipe.getWaterAmount());
        for (int i = 0; i < DRINKS_DISPLAY_SLOT; ++i) {
            ItemStack itemStack = getStack(i);
            if (itemStack.getItem().hasRecipeRemainder() && world != null) {
                Direction direction = getCachedState().get(BrewingKegBlock.FACING).rotateYCounterclockwise();
                double dropX = pos.getX() + .5d + (direction.getOffsetX() * .25d);
                double dropY = pos.getY() + .7d;
                double dropZ = pos.getZ() + .5d + (direction.getOffsetZ() * .25d);
                ItemEntity entity = new ItemEntity(world, dropX, dropY, dropZ, new ItemStack(getStack(i).getItem()
                        .getRecipeRemainder()));
                entity.setVelocity(direction.getOffsetX() * .08f, .25f, direction.getOffsetZ() * .08f);
                world.spawnEntity(entity);
            }

            if (!getStack(i).isEmpty()) {
                getStack(i).decrement(1);
            }
        }

        return true;
    }


    private void useStoredContainersOnDrink() {
        ItemStack drinkDisplay = getStack(DRINKS_DISPLAY_SLOT);
        ItemStack containerInput = getStack(CONTAINER_SLOT);
        ItemStack finalOutput = getStack(OUTPUT_SLOT);

        if (isContainerValid(containerInput) && finalOutput.getCount() < finalOutput.getMaxCount()) {
            int smallerStack = Math.min(drinkDisplay.getCount(), containerInput.getCount());
            int drinkCount = Math.min(smallerStack, drinkDisplay.getMaxCount() - finalOutput.getCount());
            if (finalOutput.isEmpty()) {
                containerInput.decrement(drinkCount);
                setStack(OUTPUT_SLOT, drinkDisplay.split(drinkCount));
            } else if (finalOutput.getItem() == drinkDisplay.getItem()) {
                drinkDisplay.decrement(drinkCount);
                containerInput.decrement(drinkCount);
                finalOutput.increment(drinkCount);
            }
        }
    }


    private boolean doesDrinkHaveContainer(ItemStack drink) {
        return !drinkContainer.isEmpty() || drink.getItem().hasRecipeRemainder();
    }

    public ItemStack getDrinkContainer() {
        if (!drinkContainer.isEmpty()) {
            return drinkContainer;
        } else {
            ItemStack drink = getDrink();
            if (drink.isEmpty()) {
                return ItemStack.EMPTY; // Return an empty ItemStack if there is no drink
            }
            ItemConvertible item = drink.getItem().getRecipeRemainder();
            if (item == null) {
                return ItemStack.EMPTY; // Return an empty ItemStack if there is no recipe remainder
            }
            return new ItemStack(item);
        }
    }


    public ItemStack useHeldItemOnDrink(ItemStack container) {
        if (isContainerValid(container) && !getDrink().isEmpty()) {
            container.decrement(1);
            return getDrink().split(1);
        }
        return ItemStack.EMPTY;
    }


    public void handleWaterBucket() {
        ItemStack waterBucketStack = this.getStack(WATER_SLOT);

        if (waterBucketStack.getItem() == Items.WATER_BUCKET) {
            boolean success = this.addWater(1000); // 1000 mb equals one bucket

            if (success) {
                // Successfully added water, replace the bucket with an empty one
                this.setStack(WATER_SLOT, new ItemStack(Items.BUCKET));
                markDirty();
            } else {
                // Handle failure case (e.g., log an error)
                System.err.println("Failed to add water to the brewing keg.");
            }
        }
    }


    public boolean addWater(long WaterAmountAdd) {
        try (Transaction transaction = Transaction.openOuter()) {
            long dropletsToAdd = FluidStack.convertMbToDroplets(WaterAmountAdd);
            long insertedAmount = this.fluidStorage.insert(FluidVariant.of(Fluids.WATER), dropletsToAdd, transaction);

            if (insertedAmount == dropletsToAdd) {
                transaction.commit();
                return true; // Successfully added water
            }
        }
        return false; // Failed to add water
    }


    private boolean hasEnoughFluid() {
        Optional<BrewingRecipe> match = getCurrentRecipe();
        if (match.isPresent()) {
            BrewingRecipe recipe = match.get();
            long requiredFluid = FluidStack.convertMbToDroplets(recipe.getWaterAmount());
            return fluidStorage.amount >= requiredFluid;
        }
        return false;
    }

    public long getWaterAmount() {
        return FluidStack.convertDropletsToMb(fluidStorage.amount);
    }

    public long getWaterCapacity() {
        return FluidStack.convertDropletsToMb(fluidStorage.getCapacity());
    }


    private void sendFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        fluidStorage.variant.toPacket(data);
        data.writeLong(fluidStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, data);
        }
    }

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }


    private Optional<BrewingRecipe> getCurrentRecipe() {
        if (world == null) return Optional.empty();
        return world.getRecipeManager().getFirstMatch(ModRecipes.BREWING, this, world);
    }


    public boolean isContainerValid(ItemStack containerItem) {
        if (containerItem.isEmpty()) {
            return false;
        }
        if (!drinkContainer.isEmpty()) {
            return ItemStack.areItemsEqual(drinkContainer, containerItem);
        } else {
            return containerItem.isOf(getDrink().getItem().getRecipeRemainder());
        }
    }



    private void extractFluid(int amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            this.fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidStack.convertMbToDroplets(amount), transaction);
            transaction.commit();
        }
    }


    public void syncFluidToClient() {
        if (this.world != null && !this.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            fluidStorage.variant.toPacket(buf);
            buf.writeLong(fluidStorage.amount);
            buf.writeBlockPos(this.pos);

            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, buf);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        syncFluidToClient();
    }


    private void moveDrinkToOutput() {
        ItemStack drinkDisplay = getStack(DRINKS_DISPLAY_SLOT);
        ItemStack finalOutput = getStack(OUTPUT_SLOT);
        ItemStack containerStack = getStack(CONTAINER_SLOT); // Get the stack from CONTAINER_SLOT

        if (containerStack.isEmpty()) {
            return; // Exit the method if CONTAINER_SLOT is empty
        }


        int drinkCount = Math.min(drinkDisplay.getCount(), drinkDisplay.getMaxCount() - finalOutput.getCount());
        if (finalOutput.isEmpty()) {
            setStack(OUTPUT_SLOT, drinkDisplay.split(drinkCount));
        } else if (finalOutput.getItem() == drinkDisplay.getItem()) {
            drinkDisplay.decrement(drinkCount);
            finalOutput.increment(drinkCount);
        }
    }


    public void trackRecipeExperience(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier recipeID = recipe.getId();
            experienceTracker.addTo(recipeID, 1);
        }
    }

    public void clearUsedRecipes(PlayerEntity player) {
        grantStoredRecipeExperience(player.getWorld(), player.getPos());
        experienceTracker.clear();
    }

    public void grantStoredRecipeExperience(World world, Vec3d pos) {
        for (Object2IntMap.Entry<Identifier> entry : experienceTracker.object2IntEntrySet()) {
            world.getRecipeManager().get(entry.getKey()).ifPresent(recipe -> splitAndSpawnExperience(world, pos, entry.getIntValue(), ((BrewingRecipe) recipe).getExperience()));
        }
    }

    private static void splitAndSpawnExperience(World world, Vec3d pos, int craftedAmount, float experience) {
        int expTotal = MathHelper.floor((float) craftedAmount * experience);
        float expFraction = MathHelper.fractionalPart((float) craftedAmount * experience);
        if (expFraction != 0.f && Math.random() < expFraction) {
            ++expTotal;
        }

        while (expTotal > 0) {
            int expValue = ExperienceOrbEntity.roundToOrbSize(expTotal);
            expTotal -= expValue;
            world.spawnEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, expValue));
        }
    }


}
