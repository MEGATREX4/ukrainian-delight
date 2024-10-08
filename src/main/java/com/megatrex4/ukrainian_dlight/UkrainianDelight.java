package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.block.FoodJarBlocks;
import com.megatrex4.ukrainian_dlight.item.ItemGroupRegistry;
import com.megatrex4.ukrainian_dlight.registry.BlockRegistry;
import com.megatrex4.ukrainian_dlight.block.entity.ModBlockEntities;
//import com.megatrex4.ukrainian_dlight.compat.patchouli.CustomBrewingRecipeProcessor;
import com.megatrex4.ukrainian_dlight.config.ModConfig;
import com.megatrex4.ukrainian_dlight.registry.initialize.CustomizeLootTables;
import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import com.megatrex4.ukrainian_dlight.item.ToolTipHelper;
import com.megatrex4.ukrainian_dlight.networking.ModMessages;
import com.megatrex4.ukrainian_dlight.registry.RecipesRegistry;
import com.megatrex4.ukrainian_dlight.registry.ScreenHandlersRegistry;
import com.megatrex4.ukrainian_dlight.registry.ScreensRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UkrainianDelight implements ModInitializer {
	public static final String MOD_ID = "ukrainian_delight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RegistryKey<DamageType> GLASS_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(MOD_ID, "glass_damage"));

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static MutableText i18n(String key, Object... args) {
		return Text.translatable(MOD_ID + "." + key, args);
	}

	@Override
	public void onInitialize() {
		FoodJarBlocks.registerFoodBlocks();
		DrinkBottleBlock.registerDrinkBlock();
		BlockRegistry.registerModBlocks();
		ItemsRegistry.registerModItems();
		ItemGroupRegistry.registerItemGroups();
		ToolTipHelper.registerTooltip();
		ModBlockEntities.registerBlockEntities();
		ScreenHandlersRegistry.registerModScreenHandlers();
		ModMessages.registerS2CPackets();
		RecipesRegistry.registerRecipes();
		ScreensRegistry.registerScreens();
		ModConfig.loadConfig();
		CustomizeLootTables.register();

		LOGGER.info("Hello Fabric world it's " + MOD_ID + "!");
	}

	public static void saveConfig() {
		ModConfig.saveConfig();
	}
}
