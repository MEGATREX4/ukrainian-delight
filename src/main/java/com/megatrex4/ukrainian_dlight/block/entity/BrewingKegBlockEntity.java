package com.megatrex4.ukrainian_dlight.block.entity;

import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.networking.ModMessages;
import com.megatrex4.ukrainian_dlight.recipe.BrewingRecipe;
import com.megatrex4.ukrainian_dlight.screen.BrewingKegScreenHandler;
import com.megatrex4.ukrainian_dlight.util.FluidStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BrewingKegBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

    //adds 6 input ingredients slot
    public static final int INGREDIENT_SLOT_1 = 0;
    public static final int INGREDIENT_SLOT_2 = 1;
    public static final int INGREDIENT_SLOT_3 = 2;
    public static final int INGREDIENT_SLOT_4 = 3;
    public static final int INGREDIENT_SLOT_5 = 4;
    public static final int INGREDIENT_SLOT_6 = 5;
    //adds container(like bottle of vial) input
    public static final int CONTAINER_SLOT = 6;
    //adds 1 input slot for water
    public static final int WATER_SLOT = 7;
    //adds 1 output slot and display slot
    public static final int DRINKS_DISPLAY_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    protected final PropertyDelegate propertyDelegate;
    private int progress;
    private int maxProgress = 200;  // Adjusted to match the brewing time in the JSON

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 20; // 20k mB
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

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("brewing_keg.progress", progress);
        nbt.put("brewing_keg.fluid_variant", fluidStorage.variant.toNbt());
        nbt.putLong("brewing_keg.fluid_amount", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("brewing_keg.progress");
        fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound("brewing_keg.fluid_variant"));
        fluidStorage.amount = nbt.getLong("brewing_keg.fluid_amount");
    }


    private void debugFluidLevel() {
        System.out.println("Fluid: " + fluidStorage.variant.getFluid().toString());
        System.out.println("Amount: " + fluidStorage.amount + " mB");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.ukrainian_delight.brewing_keg");
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

        if (isOutputSlotEmptyOrReceivable()) {
            if (this.hasRecipe() && hasEnoughFluid()) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if (hasCraftingFinished()) {
                    this.craftItem();
                    extractFluid();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private void extractFluid() {
        try (Transaction transaction = Transaction.openOuter()) {
            this.fluidStorage.extract(FluidVariant.of(Fluids.WATER), 500, transaction);
            transaction.commit();
        }
    }

    private static void transferFluidToFluidStorage(BrewingKegBlockEntity entity) {
        try(Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.insert(FluidVariant.of(Fluids.WATER),
                    FluidStack.convertDropletsToMb(FluidConstants.BUCKET), transaction);
            transaction.commit();
            entity.setStack(0, new ItemStack(Items.BUCKET));
        }
    }


    private boolean hasEnoughFluid() {
        return this.fluidStorage.amount >= 500; // mB amount!
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

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        this.removeStack(INGREDIENT_SLOT_1, 1);
        ItemStack result = new ItemStack(DrinkBottleBlock.WINE_BOTTLE);
        this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<BrewingRecipe> recipe = world.getRecipeManager().getFirstMatch(BrewingRecipe.Type.INSTANCE, this, world);
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(recipe.get().getResult()) && canInsertItemIntoOutputSlot(recipe.get().getResult().getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }
}
