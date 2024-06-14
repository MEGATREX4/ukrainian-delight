package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JarBuilder extends Item {
    private final BlockState jarBlockState;

    public JarBuilder(BlockState jarBlockState, Item.Settings settings) {
        super(settings);
        this.jarBlockState = jarBlockState;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = jarBlockState;

        // Convert ItemUsageContext to ItemPlacementContext
        net.minecraft.item.ItemPlacementContext placementContext = new net.minecraft.item.ItemPlacementContext(context);

        // Attempt to place the jar block with the correct block state
        ActionResult result = tryPlaceJar(world, pos, state, placementContext);
        if (result == ActionResult.SUCCESS) {
            context.getStack().decrement(1); // Decrement item stack if placement was successful
        }

        return result;
    }

    private ActionResult tryPlaceJar(World world, BlockPos pos, BlockState state, net.minecraft.item.ItemPlacementContext placementContext) {
        // Check if the hit side is valid and if the jar can replace the existing block
        BlockPos placePos = pos.offset(placementContext.getSide());
        BlockState targetState = world.getBlockState(placePos);
        if ((targetState.isAir() || targetState.getBlock().canReplace(targetState, placementContext)) && state.canPlaceAt(world, placePos)) {
            if (!world.isClient) {
                world.setBlockState(placePos, state); // Set the correct block state here
            }
            return ActionResult.SUCCESS;
        }

        // Check if clicked on an edge or empty space next to the block
        if (placementContext.getSide().getAxis().isHorizontal()) {
            placePos = pos.offset(placementContext.getSide().rotateYClockwise());
            targetState = world.getBlockState(placePos);
            if ((targetState.isAir() || targetState.getBlock().canReplace(targetState, placementContext)) && state.canPlaceAt(world, placePos)) {
                if (!world.isClient) {
                    world.setBlockState(placePos, state); // Set the correct block state here
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }
}
