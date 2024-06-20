package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.block.FoodJarBlocks;
import com.megatrex4.ukrainian_dlight.block.ModBlock;
import com.megatrex4.ukrainian_dlight.item.ModItemGroups;
import com.megatrex4.ukrainian_dlight.item.ModItems;
import com.megatrex4.ukrainian_dlight.item.ToolTipHelper;
import com.megatrex4.ukrainian_dlight.util.Config;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

public class UkrainianDelight implements ModInitializer {
	public static final String MOD_ID = "ukrainian_delight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RegistryKey<DamageType> GLASS_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("ukrainian_delight", "glass_damage"));


	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize() {
		RegistryKey<DamageType> GLASS_DAMAGE;

		FoodJarBlocks.registerFoodBlocks();
		DrinkBottleBlock.registerDrinkBlock();
		ModBlock.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ToolTipHelper.registerTooltip();

		LOGGER.info("Hello Fabric world it's " + MOD_ID + "!");
	}
}