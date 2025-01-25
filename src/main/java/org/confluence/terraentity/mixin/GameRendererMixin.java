package org.confluence.terraentity.mixin;

import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.confluence.terraentity.mixinauxiliary.HotSwap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {


    @Unique
    TextureTarget confluence$handTarget;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/Camera;FLorg/joml/Matrix4f;)V"))
    public void renderLevel(DeltaTracker deltaTracker, CallbackInfo ci) {
        if(confluence$handTarget == null)
            confluence$handTarget = new TextureTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height,false,false);

        HotSwap.doSomething(deltaTracker.getGameTimeDeltaPartialTick(true),confluence$handTarget);

    }
}
