package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.block.FoodBlocks;
import com.megatrex4.ukrainian_dlight.block.ModBlock;
import com.megatrex4.ukrainian_dlight.item.ModItemGroups;
import com.megatrex4.ukrainian_dlight.item.ModItems;
import com.megatrex4.ukrainian_dlight.item.ToolTipHelper;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UkrainianDelight implements ModInitializer {
	public static final String MOD_ID = "ukrainian_delight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<DamageType> GLASS_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("ukrainian_delight", "glass_damage"));

	@Override
	public void onInitialize() {
		RegistryKey<DamageType> GLASS_DAMAGE;

		FoodBlocks.registerFoodBlocks();
		ModBlock.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ToolTipHelper.registerTooltip();

		LOGGER.info("Hello Fabric world it's " + MOD_ID + "!");
	}
}