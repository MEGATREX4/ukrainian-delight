package com.megatrex4.ukrainian_dlight.registry;

import net.minecraft.util.Identifier;

public class PatternRegistry {

    private final Identifier id;
    private final String texturePath;

    public PatternRegistry(String patternName) {
        this.id = new Identifier("ukrainian_dlight", patternName);
        this.texturePath = "assets/ukrainian_delight/textures/item/krashanky/patterns/" + patternName + ".png";
    }

    public Identifier getId() {
        return id;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
