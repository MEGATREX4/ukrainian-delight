package com.megatrex4.ukrainian_dlight.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class FoodItemBuilder {
    private FoodComponent foodComponent;
    private int maxCount = 64;
    private Boolean returnsBowl = false;
    private Boolean isSnack = false;
    private Identifier bowlId = new Identifier("minecraft", "bowl");

    // Set the food component
    public FoodItemBuilder food(FoodComponent foodComponent) {
        this.foodComponent = foodComponent;
        return this;
    }

    // Set the max count
    public FoodItemBuilder maxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    // Set bowl return flag
    public FoodItemBuilder returnsBowl() {
        this.returnsBowl = true;
        return this;
    }

    public FoodItemBuilder returnsBowl(Identifier bowlId) {
        this.returnsBowl = true;
        this.bowlId = bowlId;
        return this;
    }

    // Set snack flag
    public FoodItemBuilder snack() {
        this.isSnack = true;
        return this;
    }

    public FoodItemBuilder snack(boolean isSnack) {
        this.isSnack = isSnack;
        return this;
    }

    // Build the item
    public Item build() {
        FabricItemSettings settings = new FabricItemSettings().maxCount(maxCount);
        if (foodComponent != null) {
            if (isSnack) {
                // Adjust the food component to set it as a snack
                FoodComponent.Builder foodBuilder = new FoodComponent.Builder()
                        .hunger(foodComponent.getHunger())
                        .saturationModifier(foodComponent.getSaturationModifier())
                        .snack();

                if (foodComponent.isAlwaysEdible()) {
                    foodBuilder.alwaysEdible();
                }

                foodComponent.getStatusEffects().forEach(pair ->
                        foodBuilder.statusEffect(pair.getFirst(), pair.getSecond())
                );

                foodComponent = foodBuilder.build();
            }
            settings.food(foodComponent);
        }

        // If the item returns a bowl, give the player the bowl item after consuming
        if (returnsBowl) {
            return new BowlReturningFoodItem(settings, bowlId);
        } else {
            return new ToolTipHelper(settings);
        }
    }
}
