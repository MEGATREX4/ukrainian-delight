package com.megatrex4.ukrainian_dlight.compat.patchouli;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.recipe.BrewingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrewingKegProcessor implements IComponentProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(UkrainianDelight.MOD_ID);
    private BrewingRecipe recipe;

    @Override
    public void setup(World world, IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        LOGGER.info("Ukrainian Delight: Recipe ID " + recipeId);
        this.recipe = (BrewingRecipe) world.getRecipeManager().get(new Identifier(recipeId))
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + recipeId));
    }

    @Override
    public IVariable process(World world, String key) {
        switch (key) {
            case "waterAmount":
                return IVariable.wrap(recipe.getWaterAmount());
            case "input0":
            case "input1":
            case "input2":
            case "input3":
            case "input4":
            case "input5":
                int index = Integer.parseInt(key.substring(5)) - 1;
                return IVariable.from(recipe.getIngredients().get(index).getMatchingStacks()[0]);
            case "container":
                return IVariable.from(recipe.getContainer());
            case "output":
                return IVariable.from(recipe.getOutput(world.getRegistryManager()));
            case "water":
                // Assuming `waterAmount` represents the amount of water
                return IVariable.wrap(recipe.getWaterAmount());
            case "brewingTime":
                return IVariable.wrap(recipe.getBrewingTime());
            case "text":
                return IVariable.wrap("This is how you use the BrewingKeg to create various beverages. $(br) The input items and container are required for the process, and the result will be displayed as the output item.");
            default:
                return null;
        }
    }
}