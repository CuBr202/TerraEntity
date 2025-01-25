package org.confluence.terraentity.mixinauxiliary;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.confluence.terraentity.mixin.accessor.GameRendererAccessor;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class HotSwap {
    public static boolean consume = false;
    public static void doSomething(float partialTicks, TextureTarget target){

        target.setClearColor(0, 0, 0, 0);
        target.clear(true);
        target.bindWrite(true);
        GameRenderer gr = Minecraft.getInstance().gameRenderer;
        Camera camera = gr.getMainCamera();
        Matrix4f matrix = (new Matrix4f()).rotation(camera.rotation().conjugate(new Quaternionf()));
        consume = true;
        ((GameRendererAccessor)gr).callRenderItemInHand(camera, partialTicks, matrix);

        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        target.blitToScreen(Minecraft.getInstance().getMainRenderTarget().width,Minecraft.getInstance().getMainRenderTarget().height,false);

    }
}
