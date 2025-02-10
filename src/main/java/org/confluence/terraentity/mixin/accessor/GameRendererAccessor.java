package org.confluence.terraentity.mixin.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker
    void callRenderItemInHand(PoseStack pPoseStack, Camera pActiveRenderInfo, float pPartialTicks);

}
