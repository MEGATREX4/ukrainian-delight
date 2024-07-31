package com.megatrex4.ukrainian_dlight.initialize;

import com.megatrex4.ukrainian_dlight.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class Initialise {

    public static final Identifier CHERRY_TREE_LOOT_TABLE_ID = new Identifier("minecraft", "blocks/cherry_leaves");

    public static void register() {
        // Register the event to modify loot tables
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(CHERRY_TREE_LOOT_TABLE_ID)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.CHERRY_BERRY))
                        .conditionally(RandomChanceLootCondition.builder(0.2f)) // 20% chance
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
