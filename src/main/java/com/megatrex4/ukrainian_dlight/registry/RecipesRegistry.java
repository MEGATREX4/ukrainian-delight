package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.recipe.BrewingRecipe;
import com.megatrex4.ukrainian_dlight.recipe.BrewingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipesRegistry {
    public static final RecipeType<BrewingRecipe> BREWING = RecipeType.register(new Identifier("ukrainian_delight", "brewing").toString());
    public static final RecipeSerializer<BrewingRecipe> BREWING_RECIPE_SERIALIZER = new BrewingRecipeSerializer();

    public static void registerRecipes() {

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("ukrainian_delight", "brewing"), BREWING_RECIPE_SERIALIZER);
        UkrainianDelight.LOGGER.info("Registering Recipes for " + UkrainianDelight.MOD_ID);
    }
}
