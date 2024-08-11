package com.megatrex4.ukrainian_dlight.registry.manager;

import com.megatrex4.ukrainian_dlight.registry.PatternRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public class PatternManager {
    private static final Map<String, PatternRegistry> patterns = new HashMap<>();

    static {
        // Define all patterns here
        patterns.put("border", new PatternRegistry("border"));
        patterns.put("circle", new PatternRegistry("circle"));
        patterns.put("cross", new PatternRegistry("cross"));
        patterns.put("diagonal_left", new PatternRegistry("diagonal_left"));
        patterns.put("diagonal_right", new PatternRegistry("diagonal_right"));
        patterns.put("flower", new PatternRegistry("flower"));
        patterns.put("globe", new PatternRegistry("globe"));
        patterns.put("gradient", new PatternRegistry("gradient"));
        patterns.put("gradient_up", new PatternRegistry("gradient_up"));
        patterns.put("half_vertical", new PatternRegistry("half_vertical"));
        patterns.put("half_vertical_right", new PatternRegistry("half_vertical_right"));
        patterns.put("piglin", new PatternRegistry("piglin"));
        patterns.put("rhombus", new PatternRegistry("rhombus"));
        patterns.put("small_stripes", new PatternRegistry("small_stripes"));
        patterns.put("straight_cross", new PatternRegistry("straight_cross"));
        patterns.put("stripe_bottom", new PatternRegistry("stripe_bottom"));
        patterns.put("stripe_center", new PatternRegistry("stripe_center"));
        patterns.put("stripe_downleft", new PatternRegistry("stripe_downleft"));
        patterns.put("stripe_downright", new PatternRegistry("stripe_downright"));
        patterns.put("stripe_middle", new PatternRegistry("stripe_middle"));
        patterns.put("stripe_top", new PatternRegistry("stripe_top"));
        patterns.put("triangles_bottom", new PatternRegistry("triangles_bottom"));
        patterns.put("triangles_top", new PatternRegistry("triangles_top"));
        patterns.put("triangle_bottom", new PatternRegistry("triangle_bottom"));
        patterns.put("triangle_top", new PatternRegistry("triangle_top"));
    }

    public static PatternRegistry getPatternForItem(ItemStack itemStack) {
        if (itemStack.hasNbt()) {
            NbtCompound nbt = itemStack.getNbt();
            if (nbt != null && nbt.contains("Patterns", NbtCompound.LIST_TYPE)) {
                NbtList patternList = nbt.getList("Patterns", NbtCompound.COMPOUND_TYPE);
                if (!patternList.isEmpty()) {
                    NbtCompound patternData = patternList.getCompound(0); // Assumes the first pattern is the one to use
                    String patternName = patternData.getString("Pattern");
                    return patterns.getOrDefault(patternName, patterns.get("border")); // Default to "border" if not found
                }
            }
        }
        return patterns.get("border"); // Default to "border" if no NBT data or pattern not found
    }
}
