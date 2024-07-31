package com.megatrex4.ukrainian_dlight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BrewingRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final ItemStack result;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack container;
    private final int brewingTime;
    private final float experience;
    private final int waterAmount;

    public BrewingRecipe(Identifier id, ItemStack result, DefaultedList<Ingredient> ingredients, ItemStack container, int brewingTime, float experience, int waterAmount) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
        this.container = container;
        this.brewingTime = brewingTime;
        this.experience = experience;
        this.waterAmount = waterAmount;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
        for (int i = 0; i < 6; i++) {
            ItemStack stack = inv.getStack(i);
            remainingIngredients.removeIf(ingredient -> ingredient.test(stack));
        }
        return remainingIngredients.isEmpty();
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager registryManager) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BREWING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BREWING;
    }

    public ItemStack getContainer() {
        return container;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public float getExperience() {
        return experience;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }
}
