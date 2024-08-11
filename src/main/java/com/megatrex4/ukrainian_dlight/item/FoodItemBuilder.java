package com.megatrex4.ukrainian_dlight.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class FoodItemBuilder {
    private FoodComponent foodComponent;
    private int maxCount = 64;
    private Boolean returnsBowl = false;

    public FoodItemBuilder food(FoodComponent foodComponent) {
        this.foodComponent = foodComponent;
        return this;
    }

    public FoodItemBuilder maxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public FoodItemBuilder returnsBowl() {
        this.returnsBowl = true;
        return this;
    }

    public FoodItemBuilder returnsBowl(boolean returnsBowl) {
        this.returnsBowl = returnsBowl;
        return this;
    }

    public Item build() {
        FabricItemSettings settings = new FabricItemSettings().maxCount(maxCount);
        if (foodComponent != null) {
            settings.food(foodComponent);
        }
        if (returnsBowl != null && returnsBowl) {
            return new BowlReturningFoodItem(settings);
        } else {
            return new ToolTipHelper(settings);
        }
    }
}
