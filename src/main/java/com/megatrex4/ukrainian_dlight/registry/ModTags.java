package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKeys;

public class ModTags {
    public static final TagKey<Item> LIGHT_DRINK;
    public static final TagKey<Item> MID_DRINK;
    public static final TagKey<Item> STRONG_DRINK;

    private static <E> TagKey<E> create(String pathName, RegistryKey<? extends Registry<E>> registry) {
        return TagKey.of(registry, new Identifier(UkrainianDelight.MOD_ID, pathName));
    }


    static {
        LIGHT_DRINK = create("light_drink", RegistryKeys.ITEM);
        MID_DRINK = create("mid_drink", RegistryKeys.ITEM);
        STRONG_DRINK = create("strong_drink", RegistryKeys.ITEM);
    }
}
