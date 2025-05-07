package org.confluence.terraentity.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.confluence.terraentity.mixed.colorful_light.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( value = BufferBuilder.class, priority = 999)
public abstract class BufferBuilderMixin implements VertexConsumer {
    @Inject(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), cancellable = true)
    public void addVertexMixin(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ, CallbackInfo ci) {
            LightManager.getInstance().processVertex(
                    (index)->LightManager.getVertexPos(LightManager.quadDirection.get(), LightManager.tempPos.get(), index),
                    color,
                    (newColor)->{
                        VertexConsumer.super.addVertex(x, y, z, newColor, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
                        ci.cancel();
                    },
                    ()->{}
            );

    }
}