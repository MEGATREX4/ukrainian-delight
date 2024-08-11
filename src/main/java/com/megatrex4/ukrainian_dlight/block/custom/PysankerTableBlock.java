package com.megatrex4.ukrainian_dlight.block.custom;

import com.megatrex4.ukrainian_dlight.screen.PysankersTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PysankerTableBlock extends Block {

    public PysankerTableBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(createScreenHandlerFactory(state, world, pos));
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(pos);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("container.pysankers_table");
            }

            @Override
            public ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, PlayerEntity player) {
                return new PysankersTableScreenHandler(syncId, inv, (PysankerTableBlock) state.getBlock(), new SimpleInventory(PysankersTableScreenHandler.INVENTORY_SIZE), ScreenHandlerContext.create(world, pos));
            }

        };
    }
}
