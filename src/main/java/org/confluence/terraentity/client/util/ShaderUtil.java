package org.confluence.terraentity.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.boss.renderer.BrainOfCthulhuRenderer;
import org.joml.Matrix4f;

import java.util.Objects;
import java.util.function.Consumer;

public class ShaderUtil {


    public static void shaderBlit(Matrix4f matrix4f, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        shaderBlit(matrix4f, x , x+width, y, y+height , 0, width, height,uOffset, vOffset, textureWidth, textureHeight);
    }

    static void shaderBlit(Matrix4f matrix4f, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        innerBlit(matrix4f, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float)textureWidth, (vOffset + 0.0F) / (float)textureHeight, (vOffset + (float)vHeight) / (float)textureHeight);
    }

    static void innerBlit(Matrix4f matrix4f,int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).setUv(minU, minV);
        bufferbuilder.addVertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).setUv(minU, maxV);
        bufferbuilder.addVertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).setUv(maxU, maxV);
        bufferbuilder.addVertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).setUv(maxU, minV);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }


    public static void blitScreen(ShaderInstance shader, Consumer<ShaderInstance> setupShader){


        RenderSystem.assertOnRenderThread();
        GlStateManager._disableDepthTest();
        GlStateManager._viewport(0, 0, Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);

//        ShaderInstance shader = ModRenderTypes.Shaders.colorBlitShader;
//        ShaderInstance shaderinstance = Objects.requireNonNull(shader, "Blit shader not loaded");
//        shader.COLOR_MODULATOR.set(1f, 1f, 1f, 0.2f);
//        shaderinstance.setSampler("Sampler0", Minecraft.getInstance().getMainRenderTarget());
//        shaderinstance.setSampler("Sampler1", BrainOfCthulhuRenderer.target);

        setupShader.accept(shader);

        shader.apply();
        BufferBuilder bufferbuilder = RenderSystem.renderThreadTesselator().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLIT_SCREEN);
        bufferbuilder.addVertex(0.0F, 0.0F, 0F);
        bufferbuilder.addVertex(1.0F, 0.0F, 0F);
        bufferbuilder.addVertex(1.0F, 1.0F, 0F);
        bufferbuilder.addVertex(0.0F, 1.0F, 0F);
        BufferUploader.draw(bufferbuilder.buildOrThrow());

        shader.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
        RenderSystem.disableBlend();
    }

    public static void renderDebugBlock(BufferBuilder buffer, BlockPos pos, float size, int r, int g, int b, int a){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        buffer.addVertex(x, y + size, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z + size).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y + size, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y + size, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y + size, z).setColor(r,g,b,a);

        // BOTTaddVertex()
        buffer.addVertex(x + size, y, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x + size, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y, z).setColor(r,g,b,a);
        buffer.addVertex(x, y, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y, z).setColor(r,g,b,a);

        // EdgeaddVertex()
        buffer.addVertex(x + size, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z + size).setColor(r,g,b,a);

        // EdgeaddVertex()
        buffer.addVertex(x + size, y, z).setColor(r,g,b,a);
        buffer.addVertex(x + size, y + size, z).setColor(r,g,b,a);

        // EdgeaddVertex()
        buffer.addVertex(x, y, z + size).setColor(r,g,b,a);
        buffer.addVertex(x, y + size, z + size).setColor(r,g,b,a);

        // EdgeaddVertex()
        buffer.addVertex(x, y, z).setColor(r,g,b,a);
        buffer.addVertex(x, y + size, z).setColor(r,g,b,a);
    }
}
