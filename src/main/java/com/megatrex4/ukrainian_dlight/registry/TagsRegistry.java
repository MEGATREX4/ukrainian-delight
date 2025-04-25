package com.megatrex4.ukrainian_dlight.registry;

import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import com.megatrex4.ukrainian_dlight.util.UDIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagsRegistry {

    // Nested class for Items tags
    public static class Items {
        public static final TagKey<Item> LIGHT_DRINK = createTag("light_drink");
        public static final TagKey<Item> MID_DRINK = createTag("mid_drink");
        public static final TagKey<Item> STRONG_DRINK = createTag("strong_drink");
        public static final TagKey<Item> CONTAINER = createTag("container");
        public static final TagKey<Item> KRASHANKA = createTag("krashanka");
        public static final TagKey<Item> PYSANKA = createTag("pysanka");
        public static final TagKey<Item> SALT = createTag("salt");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new UDIdentifier(name));
        }
    }
}
