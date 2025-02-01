package com.megatrex4.ukrainian_dlight.datagen.providers;

import com.megatrex4.ukrainian_dlight.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.google.gson.*;

public class LangProvider extends FabricLanguageProvider {

    private final Set<String> existingKeys = new HashSet<>();
    private final Path manualLangFile;

    public LangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
        this.manualLangFile = dataOutput.getModContainer().findPath("assets/ukrainian_delight/lang/en_us.json")
                .orElseThrow(() -> new RuntimeException("Manual lang file not found"));
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        Map<String, String> manualTranslations = loadManualTranslations();

        // Add manual translations first
        manualTranslations.forEach((key, value) -> addTranslation(translationBuilder, key, value));

        // Separate categories in output
        addTranslation(translationBuilder, "", ""); // Empty line as a separator
        addTranslation(translationBuilder, "# Items", ""); // Fake comment separator (only visual)

        // Add item translations
        ItemsRegistry.KRASHANKY_ITEMS.forEach(item -> {
            String path = item.getTranslationKey();
            String color = path.substring(path.lastIndexOf(".") + 1).replace("_krashanka", "");

            String key = "item.ukrainian_dlight." + color + "_krashanka";
            String value = capitalize(color) + " Krashanka";

            addTranslation(translationBuilder, key, value);
        });

        addTranslation(translationBuilder, "", ""); // Another separator
        addTranslation(translationBuilder, "# Blocks", ""); // Block separator

        // Add block translations (Example, replace with actual block logic)
        // BlocksRegistry.BLOCKS.forEach(block -> {
        //     addTranslation(translationBuilder, block.getTranslationKey(), generateBlockName(block));
        // });

        addTranslation(translationBuilder, "", ""); // Final separator
    }


    // Load manually written translations from en_us.json
    private Map<String, String> loadManualTranslations() {
        Map<String, String> translations = new HashMap<>();
        if (!Files.exists(manualLangFile)) {
            return translations;
        }

        try {
            String jsonContent = Files.readString(manualLangFile);
            JsonObject json = JsonParser.parseString(jsonContent).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                translations.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read manual lang file", e);
        }
        return translations;
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
