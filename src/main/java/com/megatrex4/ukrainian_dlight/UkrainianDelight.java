package com.megatrex4.ukrainian_dlight;

import com.megatrex4.ukrainian_dlight.block.FoodBlocks;
import com.megatrex4.ukrainian_dlight.block.ModBlock;
import com.megatrex4.ukrainian_dlight.item.ModItemGroups;
import com.megatrex4.ukrainian_dlight.item.ModItems;
import com.megatrex4.ukrainian_dlight.item.ToolTipHelper;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UkrainianDelight implements ModInitializer {
	public static final String MOD_ID = "ukrainian_delight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		FoodBlocks.registerFoodBlocks();
		ModBlock.registerModBlocks();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ToolTipHelper.registerTooltip();

		BlockRenderLayerMap.INSTANCE.putBlock(FoodBlocks.JAR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FoodBlocks.APPLE_JAM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FoodBlocks.CANNED_TOMATOES, RenderLayer.getCutout());


		LOGGER.info("Hello Fabric world it's " + MOD_ID + "!");
	}
}