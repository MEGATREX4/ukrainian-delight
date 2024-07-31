package com.megatrex4.ukrainian_dlight.compat.jade;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.screen.PlayerScreenHandler;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.overlay.OverlayRenderer;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.FluidTextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import org.joml.Matrix4f;

public class CustomFluidRenderer {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void drawFluid(DrawContext guiGraphics, float xPosition, float yPosition, JadeFluidObject fluid, float width, float height, long capacityMb) {
        if (!fluid.isEmpty()) {
            long amount = fluid.getAmount();  // Assuming `getAmount()` gets the current fluid amount in mB
            long capacity = capacityMb;
            float heightRatio = (float) amount / (float) capacity;

            // Adjust scaling if the amount is too small
            float scaledHeight = height * Math.max(heightRatio, 0.01F);

            ClientProxy.getFluidSpriteAndColor(fluid, (sprite, color) -> {
                if (sprite == null) {
                    float maxY = yPosition + height;
                    if (color == -1) {
                        color = -1431655766;
                    }
                    fill(guiGraphics, xPosition, maxY - scaledHeight, xPosition + width, maxY, color);
                } else {
                    if (OverlayRenderer.alpha != 1.0F) {
                        color = IWailaConfig.IConfigOverlay.applyAlpha(color, OverlayRenderer.alpha);
                    }
                    drawTiledSprite(guiGraphics, xPosition, yPosition, width, height, color, scaledHeight, sprite);
                }
            });
        }
    }

    private static void drawTiledSprite(DrawContext guiGraphics, float xPosition, float yPosition, float tiledWidth, float tiledHeight, int color, float scaledAmount, Sprite sprite) {
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        Matrix4f matrix = guiGraphics.getMatrices().peek().getPositionMatrix();
        setGLColorFromInt(color);
        RenderSystem.enableBlend();
        int xTileCount = (int) (tiledWidth / 16.0F);
        float xRemainder = tiledWidth - (xTileCount * 16);
        int yTileCount = (int) (scaledAmount / 16.0F);
        float yRemainder = scaledAmount - (yTileCount * 16);
        float yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; ++xTile) {
            for (int yTile = 0; yTile <= yTileCount; ++yTile) {
                float width = xTile == xTileCount ? xRemainder : 16.0F;
                float height = yTile == yTileCount ? yRemainder : 16.0F;
                float x = xPosition + (xTile * 16);
                float y = yStart - ((yTile + 1) * 16);
                if (width > 0.0F && height > 0.0F) {
                    float maskTop = 16.0F - height;
                    float maskRight = 16.0F - width;
                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 0.0F);
                }
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, Sprite textureSprite, float maskTop, float maskRight, float zLevel) {
        float uMin = textureSprite.getMinU();
        float uMax = textureSprite.getMaxU();
        float vMin = textureSprite.getMinV();
        float vMax = textureSprite.getMaxV();
        uMax -= maskRight / 16.0F * (uMax - uMin);
        vMax -= maskTop / 16.0F * (vMax - vMin);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16.0F, zLevel).texture(uMin, vMax).next();
        bufferBuilder.vertex(matrix, xCoord + 16.0F - maskRight, yCoord + 16.0F, zLevel).texture(uMax, vMax).next();
        bufferBuilder.vertex(matrix, xCoord + 16.0F - maskRight, yCoord + maskTop, zLevel).texture(uMax, vMin).next();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).texture(uMin, vMin).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    private static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void fill(DrawContext guiGraphics, float minX, float minY, float maxX, float maxY, int color) {
        fill(guiGraphics.getMatrices().peek().getPositionMatrix(), minX, minY, maxX, maxY, color);
    }

    private static void fill(Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color) {
        float f3;
        if (minX < maxX) {
            f3 = minX;
            minX = maxX;
            maxX = f3;
        }

        if (minY < maxY) {
            f3 = minY;
            minY = maxY;
            maxY = f3;
        }

        f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(matrix, minX, maxY, 0.0F).color(f, f1, f2, f3).next();
        bufferbuilder.vertex(matrix, maxX, maxY, 0.0F).color(f, f1, f2, f3).next();
        bufferbuilder.vertex(matrix, maxX, minY, 0.0F).color(f, f1, f2, f3).next();
        bufferbuilder.vertex(matrix, minX, minY, 0.0F).color(f, f1, f2, f3).next();
        BufferRenderer.drawWithGlobalProgram(bufferbuilder.end());
        RenderSystem.disableBlend();
    }
}
