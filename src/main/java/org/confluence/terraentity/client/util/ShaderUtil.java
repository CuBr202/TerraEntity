package org.confluence.terraentity.client.util;

import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix4f;

public class ShaderUtil {

    public static void shaderBlit(Matrix4f matrix4f, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        shaderBlit(matrix4f, x , x+width, y, y+height , 0, width, height,uOffset, vOffset, textureWidth, textureHeight);
    }

    static void shaderBlit(Matrix4f matrix4f, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        innerBlit(matrix4f, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float)textureWidth, (vOffset + 0.0F) / (float)textureHeight, (vOffset + (float)vHeight) / (float)textureHeight);
    }

    static void innerBlit(Matrix4f matrix4f,int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).uv(minU, minV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).uv(maxU, minV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
