package com.megatrex4.ukrainian_dlight.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

public class FoodBlockItem extends BlockItem {
    public FoodBlockItem(Block block, FoodComponent foodComponent) {
        super(block, new Item.Settings().food(foodComponent));
    }
}
