package com.megatrex4.ukrainian_dlight.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BrewingRecipeSerializer implements RecipeSerializer<BrewingRecipe> {

    @Override
    public BrewingRecipe read(Identifier id, JsonObject json) {
        return BrewingRecipe.fromJson(id, json);
    }

    @Override
    public BrewingRecipe read(Identifier id, PacketByteBuf buf) {
        // Implement packet reading logic if necessary
        return null;
    }

    @Override
    public void write(PacketByteBuf buf, BrewingRecipe recipe) {
        // Implement packet writing logic if necessary
    }

    public static final RecipeSerializer<BrewingRecipe> INSTANCE = new BrewingRecipeSerializer();
    public static final Identifier ID = new Identifier("ukrainian_dlight", "brewing");

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, ID, INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, ID, new RecipeType<BrewingRecipe>() {
            @Override
            public String toString() {
                return ID.toString();
            }
        });
    }
}
