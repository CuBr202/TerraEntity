package org.confluence.terraentity.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.confluence.terraentity.mixed.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( value = BufferBuilder.class, priority = 999)
public abstract class BufferBuilderMixin implements VertexConsumer {
    @Inject(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), cancellable = true)
    public void addVertexMixin(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ, CallbackInfo ci) {
        if (LightManager.tempPos.get() != null && LightManager.quad.get() != null) {
            // 读取当前线程的独立副本
            float localR = LightManager.r.get();
            float localG = LightManager.g.get();
            float localB = LightManager.b.get();
            BakedQuad localQuad = LightManager.quad.get();

            int newColor = LightManager.getVertexLightColor(
                    localR, localG, localB,
                    localQuad,
                    LightManager.tempPos.get(),
                    color
            );
            if(color != newColor ) {
                VertexConsumer.super.addVertex(x, y, z, color, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
                ci.cancel();
            }
        }
    }
}