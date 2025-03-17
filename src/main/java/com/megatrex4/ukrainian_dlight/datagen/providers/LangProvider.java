package com.megatrex4.ukrainian_dlight.datagen.providers;

import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class LangProvider extends FabricLanguageProvider {

    private final Set<String> existingKeys = new HashSet<>();

    public LangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {

        // Add item translations
        ItemsRegistry.KRASHANKY_ITEMS.forEach(item -> {
            String path = item.getTranslationKey();
            String color = path.substring(path.lastIndexOf(".") + 1).replace("_krashanka", "");

            String key = "item.ukrainian_dlight." + color + "_krashanka";
            String value = capitalize(color) + " Krashanka";

            addTranslation(translationBuilder, key, value);
        });

        addTranslation(translationBuilder, "itemgroup.meals_ukrainian_delight", "UD Meals");
        addTranslation(translationBuilder, "itemgroup.ingredients_ukrainian_delight", "UD Ingredients");
        addTranslation(translationBuilder, "itemgroup.jars_ukrainian_delight", "UD Jars");
        addTranslation(translationBuilder, "itemgroup.krashanky_ukrainian_delight", "UD Krashanky");
        addTranslation(translationBuilder, "rei.ukrainian_delight", "Ukrainian Delight");

        addItemTranslation(translationBuilder, "horseradish");
        addItemTranslation(translationBuilder, "salt");
        addItemTranslation(translationBuilder, "cucumber");
        addItemTranslation(translationBuilder, "cutted_cucumber");
        addItemTranslation(translationBuilder, "apple_slice");
        addItemTranslation(translationBuilder, "dried_apple_slice");
        addItemTranslation(translationBuilder, "cherry_berry");
        addItemTranslation(translationBuilder, "cottage_cheese");
        addItemTranslation(translationBuilder, "salo");
        addItemTranslation(translationBuilder, "yeast");

        // Add block translations
        addBlockTranslation(translationBuilder, "cucumber_crate");
        addBlockTranslation(translationBuilder, "jar");
        addBlockTranslation(translationBuilder, "apple_jam");
        addBlockTranslation(translationBuilder, "jarred_tomatoes");
        addBlockTranslation(translationBuilder, "jarred_cabbage");
        addBlockTranslation(translationBuilder, "jarred_beetroot");
        addBlockTranslation(translationBuilder, "jarred_carrot");
        addBlockTranslation(translationBuilder, "jarred_onion");
        addBlockTranslation(translationBuilder, "jarred_borscht");
        addBlockTranslation(translationBuilder, "wine_bottle");
        addBlockTranslation(translationBuilder, "brewing_keg");
        addBlockTranslation(translationBuilder, "salt_block");
        addBlockTranslation(translationBuilder, "salt_bag");

        addTranslation(translationBuilder, "ukrainian_delight.advancement.root", "Ukrainian Delight");
        addTranslation(translationBuilder, "ukrainian_delight.advancement.root.desc", "The power of Ukrainian delicacies!");

        addTranslation(translationBuilder, "ukrainian_delight.advancement.borscht", "Where to find red water!");
        addTranslation(translationBuilder, "ukrainian_delight.advancement.borscht.desc", "Cook borscht");
        addTranslation(translationBuilder, "ukrainian_delight.advancement.lean_borscht", "The same, but with mushrooms!");
        addTranslation(translationBuilder, "ukrainian_delight.advancement.lean_borscht.desc", "Cook lean borscht");

        addTranslation(translationBuilder, "gui.ukrainian_delight.brewing_keg", "Brewing Keg");
        addTranslation(translationBuilder, "death.attack.glass_damage", "%s was not careful with glass");
        addTranslation(translationBuilder, "tooltip.ukrainian_delight.no_effects", "No effects");
        addTranslation(translationBuilder, "biome.ukrainian_delight.salt_caves", "Salt Caves");

        // Item and tank tooltips
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.tank_amount_with_capacity", "%d / %d mB");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.tank_amount", "%d mB");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.tank_empty", "Empty");

        addTranslation(translationBuilder, "ukrainian_delight.tooltip.poured", "Poured into %s");

        addTranslation(translationBuilder, "ukrainian_delight.tooltip.item_amout_with_capacity", "[%s] %d / %d mB");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.item_tank_amount", "[%s] %d mB");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.empty", "Empty");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.single_serving", "Holds 1 drink of:");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.many_servings", "Holds %s drinks of:");

        addTranslation(translationBuilder, "ukrainian_delight.tooltip.light_drink", "Light Drink");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.mid_drink", "Medium Drink");
        addTranslation(translationBuilder, "ukrainian_delight.tooltip.strong_drink", "Strong Drink");

        // Config GUI
        addTranslation(translationBuilder, "ukrainian_delight.title.config", "Ukrainian Delight Configuration");
        addTranslation(translationBuilder, "ukrainian_delight.category.general", "General Settings");
        addTranslation(translationBuilder, "ukrainian_delight.option.brewing_keg_capacity", "Brewing Keg Capacity");
        addTranslation(translationBuilder, "tooltip.ukrainian_delight.brewing_keg_capacity", "%s");
        addTranslation(translationBuilder, "tooltip.ukrainian_delight.brewing_keg_amount", "%s");

        // Patchouli guide
        addTranslation(translationBuilder, "patchouli.ukrainian_delight.guide_name", "Mum's recipes");
        addTranslation(translationBuilder, "patchouli.ukrainian_delight.landing_text", "Welcome to the Ukrainian Delight Mod Guide!");
        addTranslation(translationBuilder, "patchouli.ukrainian_delight.tooltip.tank_amount", "%s mB of water");

    }

    private void addItemTranslation(TranslationBuilder translationBuilder, String value) {
        String key = "item.ukrainian_delight." + value.toLowerCase().replace(" ", "_");
        String formattedValue = capitalize(value.replace("_", " "));
        addTranslation(translationBuilder, key, formattedValue);
    }

    private void addBlockTranslation(TranslationBuilder translationBuilder, String value) {
        String key = "block.ukrainian_delight." + value.toLowerCase().replace(" ", "_");
        String formattedValue = capitalize(value.replace("_", " "));
        addTranslation(translationBuilder, key, formattedValue);
    }

    // Avoid duplicate translations
    private void addTranslation(TranslationBuilder translationBuilder, String key, String value) {
        if (!existingKeys.contains(key)) {
            translationBuilder.add(key, value);
            existingKeys.add(key);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}
