package com.megatrex4.ukrainian_dlight.registry.initialize;

import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class CustomizeLootTables {

    // Define the vanilla loot table ID you want to modify
    public static final Identifier CHERRY_TREE_LOOT_TABLE_ID = new Identifier("minecraft", "blocks/cherry_leaves");

    public static void register() {
        // Register the event to modify loot tables
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(CHERRY_TREE_LOOT_TABLE_ID)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(LootTableEntry.builder(new Identifier("ukrainian_delight", "blocks/inject/cherry_leaves")));
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
