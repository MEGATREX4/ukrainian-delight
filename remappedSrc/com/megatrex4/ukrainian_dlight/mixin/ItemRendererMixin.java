package com.megatrex4.ukrainian_dlight.mixin;

import com.megatrex4.ukrainian_dlight.block.DrinkBottleBlock;
import com.megatrex4.ukrainian_dlight.UkrainianDelight;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    //@ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    //public BakedModel useBottleModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    //    if (stack.isOf(DrinkBottleBlock.BOTTLE.asItem()) && renderMode != ModelTransformationMode.GUI) {
    //        return ((ItemRendererAccessor) this).ukrainian_dlight$getItemModels().getModelManager().getModel(new ModelIdentifier(UkrainianDelight.MOD_ID, "bottle_inventory", "inventory"));
    //    }
    //    return value;
    //}
}
