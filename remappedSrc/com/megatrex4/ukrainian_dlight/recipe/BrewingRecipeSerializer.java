package com.megatrex4.ukrainian_dlight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class BrewingRecipeSerializer implements RecipeSerializer<BrewingRecipe> {

    @Override
    public BrewingRecipe read(Identifier id, JsonObject json) {
        ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
        JsonArray ingredientsArray = JsonHelper.getArray(json, "ingredients");
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(ingredientsArray.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredientsArray.size(); i++) {
            JsonObject ingredientObject = ingredientsArray.get(i).getAsJsonObject();
            ingredients.set(i, Ingredient.fromJson(ingredientObject));
        }

        ItemStack container = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "container"));
        int brewingTime = JsonHelper.getInt(json, "brewingtime");
        float experience = JsonHelper.getFloat(json, "experience");
        int waterAmount = JsonHelper.getInt(json, "water");

        return new BrewingRecipe(id, result, ingredients, container, brewingTime, experience, waterAmount);
    }

    @Override
    public BrewingRecipe read(Identifier id, PacketByteBuf buf) {
        int size = buf.readInt();
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(size, Ingredient.EMPTY);
        for (int i = 0; i < size; i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        ItemStack container = buf.readItemStack();
        ItemStack result = buf.readItemStack();
        int brewingTime = buf.readInt();
        float experience = buf.readFloat();
        int waterAmount = buf.readInt();
        return new BrewingRecipe(id, result, ingredients, container, brewingTime, experience, waterAmount);
    }

    @Override
    public void write(PacketByteBuf buf, BrewingRecipe recipe) {
        buf.writeInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buf);
        }
        buf.writeItemStack(recipe.getContainer());
        buf.writeItemStack(recipe.getOutput(null)); // The registryManager parameter is not used here.
        buf.writeInt(recipe.getBrewingTime());
        buf.writeFloat(recipe.getExperience());
        buf.writeInt(recipe.getWaterAmount());
    }
}
