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

import com.nhoryzon.mc.farmersdelight.entity.block.CookingPotBlockEntity;
import io.netty.buffer.Unpooled;
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

    public static final String TAG_KEY_COOK_RECIPES_USED = "RecipesUsed";

    private long capacity;

    public static final int[] INGREDIENT_SLOTS = {0, 1, 2, 3, 4, 5};
    public static final int CONTAINER_SLOT = 6;
    public static final int WATER_SLOT = 8;
    public static final int DRINKS_DISPLAY_SLOT = 9;
    public static final int OUTPUT_SLOT = 10;

    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

    // Define OUTPUT_SLOTS and ALL_SLOTS_EXCEPT_INGREDIENTS
    private static final int[] OUTPUT_SLOTS = {OUTPUT_SLOT};
    private static final int[] ALL_SLOTS_EXCEPT_INGREDIENTS = {CONTAINER_SLOT, WATER_SLOT, DRINKS_DISPLAY_SLOT, OUTPUT_SLOT};


    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> experienceTracker;
    private int progress;
    private int maxProgress = 200;  // Adjusted to match the brewing time in the JSON
    private float totalExperience = 0;
    private Text customName;
    private ItemStack drinkContainer;



    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }


        @Override
        public long getCapacity(FluidVariant variant) {
            return FluidStack.convertMbToDroplets(ModConfig.getBrewingKegCapacity());
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
    public DefaultedList<ItemStack> getItems() {
        return inventory;
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
        writeInventoryNbt(tag);

        // Save totalExperience
        tag.putFloat("TotalExperience", totalExperience);

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

        // Load totalExperience
        if (tag.contains("TotalExperience", NbtType.FLOAT)) {
            totalExperience = tag.getFloat("TotalExperience");
        }

        drinkContainer = ItemStack.fromNbt(tag.getCompound(CompoundTagUtils.TAG_KEY_CONTAINER));

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

        NbtCompound compoundRecipes = tag.getCompound(TAG_KEY_COOK_RECIPES_USED);
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




    private void debugFluidLevel() {
        System.out.println("Fluid Variant: " + fluidStorage.variant.getFluid().toString());
        System.out.println("Fluid Amount: " + fluidStorage.amount + " mB");
        System.out.println("Fluid Capacity: " + fluidStorage.getCapacity() + " mB");
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

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient) {
            return;
        }
        boolean dirty = false;

        handleWaterBucket();
        processCrafting();
        processItemTransfer(world, pos, state);

    }

    private void processItemTransfer(World world, BlockPos pos, BlockState state) {
        ItemStack displayStack = getStack(DRINKS_DISPLAY_SLOT);
        ItemStack containerStack = getStack(CONTAINER_SLOT);
        ItemStack outputStack = getStack(OUTPUT_SLOT);
        ItemStack requiredContainer = getDrinkContainer();

        if (!displayStack.isEmpty() && !containerStack.isEmpty() && containerStack.getItem() == requiredContainer.getItem()) {
            if (isOutputSlotEmptyOrReceivable(outputStack)) {
                containerStack.decrement(1);

                if (outputStack.isEmpty()) {
                    setStack(OUTPUT_SLOT, new ItemStack(displayStack.getItem(), 1));
                    displayStack.decrement(1);
                } else if (ItemStack.canCombine(outputStack, displayStack)) {
                    int maxCount = outputStack.getMaxCount();
                    if (outputStack.getCount() < maxCount) {
                        outputStack.increment(1);
                        displayStack.decrement(1);
                    }
                }

                if (displayStack.isEmpty()) {
                    setStack(DRINKS_DISPLAY_SLOT, ItemStack.EMPTY);
                }

                markDirty(); // Mark the block entity as dirty
                giveExperience(world, pos);
            }
        }
    }




    private void giveExperience(World world, BlockPos pos) {
        // Calculate experience to give based on totalExperience
        int xpToGive = MathHelper.floor(totalExperience);
        if (xpToGive > 0) {

            totalExperience = 0;

            spawnExperienceOrb(world, pos, xpToGive);
            markDirty();
        }
    }

    private void spawnExperienceOrb(World world, BlockPos pos, int xpAmount) {
        ExperienceOrbEntity orb = new ExperienceOrbEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, xpAmount);
        world.spawnEntity(orb);
    }



    private void processCrafting() {
        Optional<BrewingRecipe> match = getCurrentRecipe();
        if (match.isPresent()) {
            BrewingRecipe recipe = match.get();

            boolean validIngredients = hasValidIngredients(recipe);
            boolean displaySlotReceivable = isDisplaySlotEmptyOrReceivable(recipe.craft(this, world.getRegistryManager()));
            boolean hasRecipe = hasRecipe();
            boolean enoughFluid = hasEnoughFluid();

            if (validIngredients && displaySlotReceivable && hasRecipe && enoughFluid) {
                // Calculate crafted amount, assuming 1 for now
                int craftedAmount = 1;
                if (this.craftItem(recipe, craftedAmount)) { // Provide craftedAmount here
                    this.resetProgress();
                    playBrewingSound();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
        }
    }

    private void playBrewingSound() {
        if (world != null && !world.isClient) {
            world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }


    public ItemStack getDrinkContainer() {
        if (!drinkContainer.isEmpty()) {
            return drinkContainer;
        } else {
            return new ItemStack(getDrink().getItem().getRecipeRemainder());
        }
    }







    private boolean hasValidIngredients(BrewingRecipe recipe) {
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot : INGREDIENT_SLOTS) {
            ingredients.add(getStack(slot));
        }

        List<Ingredient> recipeIngredients = recipe.getIngredients();

        // Check if the number of ingredients in the recipe is greater than the number of slots
        if (recipeIngredients.size() > ingredients.size()) {
            return false;
        }

        // Check that each ingredient in the recipe can be found in the ingredient slots
        for (Ingredient ingredient : recipeIngredients) {
            boolean foundMatch = false;
            for (ItemStack stack : ingredients) {
                if (ingredient.test(stack)) {
                    foundMatch = true;
                    // Remove the matched ingredient to prevent duplicate matching
                    ingredients.remove(stack);
                    break;
                }
            }
            if (!foundMatch) {
                return false;
            }
        }

        // Ensure the remaining slots are empty
        for (ItemStack stack : ingredients) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }


    private boolean isOutputSlotEmptyOrReceivable(ItemStack output) {
        ItemStack outputStack = this.getStack(OUTPUT_SLOT);
        return outputStack.isEmpty() || (ItemStack.canCombine(outputStack, output) && outputStack.getCount() < outputStack.getMaxCount());
    }


    public void handleWaterBucket() {
        ItemStack waterBucketStack = this.getStack(WATER_SLOT);

        if (waterBucketStack.getItem() == Items.WATER_BUCKET) {
            boolean success = this.addWater(1000); // 1000 mb equals one bucket

            if (success) {
                // Successfully added water, replace the bucket with an empty one
                this.setStack(WATER_SLOT, new ItemStack(Items.BUCKET));
                markDirty();
                sendFluidPacket();

                // Play water pouring sound effect
                if (!this.world.isClient) {
                    this.world.playSound(null, this.pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
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



    private static void transferFluidToFluidStorage(BrewingKegBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            // Directly insert the fluid in millibuckets
            entity.fluidStorage.insert(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction);
            transaction.commit();
            entity.setStack(0, new ItemStack(Items.BUCKET));
        }
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



    public void sendFluidPacket() {
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


    private void resetProgress() {
        this.progress = 0;
    }


    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }


    private void increaseCraftProgress() {
        progress++;
    }


    private Optional<BrewingRecipe> getCurrentRecipe() {
        if (world == null) return Optional.empty();
        return world.getRecipeManager().getFirstMatch(ModRecipes.BREWING, this, world);
    }

    private boolean hasRecipe() {
        Optional<BrewingRecipe> match = getCurrentRecipe();
        if (match.isPresent()) {
            BrewingRecipe recipe = match.get();
            boolean hasWater = fluidStorage.amount >= recipe.getWaterAmount();
            return hasWater && match.get().matches(this, world);
        }
        return false;
    }


    private boolean craftItem(BrewingRecipe recipe, int craftedAmount) {
        if (this.world != null && recipe != null) {
            ++this.progress;
            this.maxProgress = recipe.getBrewingTime();

            if (this.progress < this.maxProgress) {
                return false;
            } else {
                this.progress = 0;
                ItemStack recipeOutput = recipe.craft(this, this.world.getRegistryManager());
                ItemStack currentOutput = this.getStack(DRINKS_DISPLAY_SLOT);

                drinkContainer = recipe.getContainer();

                if (currentOutput.isEmpty()) {
                    this.setStack(DRINKS_DISPLAY_SLOT, recipeOutput.copy());
                } else if (currentOutput.getItem() == recipeOutput.getItem()) {
                    currentOutput.increment(recipeOutput.getCount());
                }


                // Handle item remainder logic
                handleRecipeRemainder(recipe);

                // Decrement liquid amount here using extractFluid
                this.extractFluid(recipe.getWaterAmount());

                // Calculate total experience and track it
                float recipeExperience = recipe.getExperience();
                float totalExperience = recipeExperience * craftedAmount;
                this.trackRecipeExperience(totalExperience);

                this.world.playSound(null, this.pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                markDirty();
                return true;
            }
        } else {
            return false;
        }
    }




    private void handleRecipeRemainder(BrewingRecipe recipe) {
        for (int i = 0; i < 6; ++i) {
            ItemStack itemStack = this.getStack(i);
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.test(itemStack)) {
                    if (itemStack.getItem().hasRecipeRemainder() && this.world != null) {
                        Direction direction = this.getCachedState().get(BrewingKegBlock.FACING).rotateYCounterclockwise();
                        double dropX = this.pos.getX() + 0.5 + direction.getOffsetX() * 0.25;
                        double dropY = this.pos.getY() + 0.7;
                        double dropZ = this.pos.getZ() + 0.5 + direction.getOffsetZ() * 0.25;
                        ItemEntity entity = new ItemEntity(this.world, dropX, dropY, dropZ, new ItemStack(itemStack.getItem().getRecipeRemainder()));
                        entity.setVelocity(direction.getOffsetX() * 0.08F, 0.25, direction.getOffsetZ() * 0.08F);
                        this.world.spawnEntity(entity);
                    }

                    itemStack.decrement(1);
                    break;
                }
            }
        }
    }





    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (isContainerValid(container) && !getDrink().isEmpty()) {
            container.decrement(1);
            return getDrink().split(1);
        }
        return ItemStack.EMPTY;
    }

    private boolean doesDrinkHaveContainer(ItemStack meal) {
        return !drinkContainer.isEmpty() || meal.getItem().hasRecipeRemainder();
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

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isDisplaySlotEmptyOrReceivable(ItemStack output) {
        ItemStack displayStack = this.getStack(DRINKS_DISPLAY_SLOT);
        return displayStack.isEmpty() || (ItemStack.canCombine(displayStack, output) && displayStack.getCount() < displayStack.getMaxCount());
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


    public void trackRecipeExperience(float totalExperience) {
        this.totalExperience += totalExperience;
        markDirty();
    }



    private void moveDrinkToOutput() {
        ItemStack dinkDisplay = getStack(DRINKS_DISPLAY_SLOT);
        ItemStack finalOutput = getStack(OUTPUT_SLOT);
        int mealCount = Math.min(dinkDisplay.getCount(), dinkDisplay.getMaxCount() - finalOutput.getCount());
        if (finalOutput.isEmpty()) {
            setStack(OUTPUT_SLOT, dinkDisplay.split(mealCount));
        } else if (finalOutput.getItem() == dinkDisplay.getItem()) {
            dinkDisplay.decrement(mealCount);
            finalOutput.increment(mealCount);
        }
    }






}
