package com.megatrex4.ukrainian_dlight.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class ItemBuilder {
    private FoodComponent foodComponent;
    private int maxCount = 64;
    private Boolean returnsBowl = null;

    public ItemBuilder food(FoodComponent foodComponent) {
        this.foodComponent = foodComponent;
        return this;
    }

    public ItemBuilder maxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public ItemBuilder returnsBowl() {
        this.returnsBowl = true;
        return this;
    }

    public ItemBuilder returnsBowl(boolean returnsBowl) {
        this.returnsBowl = returnsBowl;
        return this;
    }

    public Item build() {
        FabricItemSettings settings = new FabricItemSettings().maxCount(maxCount);
        if (foodComponent != null) {
            settings.food(foodComponent);
        }
        if (returnsBowl == null || returnsBowl) {
            return new BowlReturningFoodItem(settings);
        } else {
            return new ToolTipHelper(settings);
        }
    }
}
