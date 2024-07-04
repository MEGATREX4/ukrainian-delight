package com.megatrex4.ukrainian_dlight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.registry.DynamicRegistryManager;

public class BrewingRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack result;
    private final int brewingTime;
    private final float experience;
    // private final int waterAmount;
    private final ItemStack container;

    public BrewingRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack result, int brewingTime, float experience,
                         //int waterAmount,
                         ItemStack container) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.brewingTime = brewingTime;
        this.experience = experience;
        // this.waterAmount = waterAmount;
        this.container = container;
    }

    public static BrewingRecipe fromJson(Identifier id, JsonObject json) {
        JsonArray ingredientsArray = JsonHelper.getArray(json, "ingredients");
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(ingredientsArray.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredientsArray.size(); i++) {
            ingredients.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
        }

        ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
        int brewingTime = JsonHelper.getInt(json, "brewingtime");
        float experience = JsonHelper.getFloat(json, "experience");
        //int waterAmount = JsonHelper.getInt(json, "water");
        ItemStack container = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "container"));

        return new BrewingRecipe(id, ingredients, result, brewingTime, experience,
                //waterAmount,
                container);
    }

    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public float getExperience() {
        return experience;
    }

//    public int getWaterAmount() {
//        return waterAmount;
//    }

    public ItemStack getContainer() {
        return container;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        // Implement matching logic
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager registryManager) {
        return getResult().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getResult();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BrewingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BrewingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "brewing";
    }
}
