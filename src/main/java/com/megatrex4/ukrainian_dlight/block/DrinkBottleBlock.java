package com.megatrex4.ukrainian_dlight.block;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.block.custom.BottleBlock;
import com.megatrex4.ukrainian_dlight.item.DrinkBlockItem;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkBottleBlock {
    public static final Block WINE_BOTTLE = registerBlock("wine_bottle", new BottleBlock(), new FoodComponent.Builder()
            .hunger(6).saturationModifier(0.6f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 30 * 20, 3), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 30 * 20, 3), 1.0f)
            .alwaysEdible()
            .build());

    private static Block registerBlock(String name, Block block, FoodComponent foodComponent) {
        registerBlockItem(name, block, foodComponent);
        return Registry.register(Registries.BLOCK, new Identifier(UkrainianDelight.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, FoodComponent foodComponent) {
        Item item;
        if (foodComponent != null) {
            item = new DrinkBlockItem(block, foodComponent);
        } else {
            item = new BlockItem(block, new Item.Settings());
        }
        return Registry.register(Registries.ITEM, new Identifier(UkrainianDelight.MOD_ID, name), item);
    }

    public static void registerDrinkBlock() {
        UkrainianDelight.LOGGER.info("Registering Drink Blocks for " + UkrainianDelight.MOD_ID);
    }
}
