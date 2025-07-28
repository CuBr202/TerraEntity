package org.confluence.terraentity.client.buffer;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.mixed.IShaderInstance;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * 用于在渲染器中处理，后处理渲染额外的内容 的缓冲区管理器
 */
public class NPCChatBubbleBuffer extends AbstractBufferManager {

    Queue<RenderData> renderQueue = new ConcurrentLinkedQueue<>();

    static volatile NPCChatBubbleBuffer instance;

    public static NPCChatBubbleBuffer getInstance() {
        if (instance == null) {
            synchronized (NPCChatBubbleBuffer.class) {
                if (instance == null) {
                    instance = new NPCChatBubbleBuffer(1000);
                }
            }
        }
        return instance;
    }

    public record RenderData(BufferBuilder buffer, TextureTarget target, float progress, double dist){

    }

    public void addRenderData(BufferBuilder buffer, TextureTarget target, float progress, double dist) {
        renderQueue.add(new RenderData(buffer, target, progress, dist));
    }
    /**
     * @param refreshTime 刷新间隔，单位毫秒
     */
    public NPCChatBubbleBuffer(int refreshTime) {
        super(refreshTime);
    }

    @Override
    public void render(PoseStack poseStack, Matrix4f modelMatrix, Vec3 playerPos, Matrix4f projectMatrix) {
        if(!this.renderQueue.isEmpty()) {
            var oldShader = RenderSystem.getShader();
            Iterator<RenderData> datas = this.renderQueue.iterator();

            Matrix4f model = new Matrix4f()
                    .rotate(new Quaternionf(Minecraft.getInstance().gameRenderer.getMainCamera()
                            .rotation())
                            .conjugate());
            RenderSystem.getModelViewMatrix().set(model);

            while (datas.hasNext()) {
                RenderData data = datas.next();
                MeshData meshData = data.buffer.build();
//
//
                if (meshData != null) {
                    beforeRender();
                    RenderSystem.setShader(setShader());
                    RenderSystem.setShaderTexture(0, data.target.getColorTextureId());
                    RenderSystem.setShaderTexture(1, TerraEntity.space("textures/gui/noise.png"));
                    IShaderInstance shader = (IShaderInstance) ModRenderTypes.Shaders.pixelStyleBlitShader;
                    shader.getTerra_entity$Progress().set(data.progress);
                    shader.getTerra_entity$PixelSize().set(32f);

                    poseStack.pushPose();

                    BufferUploader.drawWithShader(meshData);

                    VertexBuffer.unbind();
                    poseStack.popPose();
                }
            }


            afterRender(poseStack);
            this.renderQueue.clear();
            RenderSystem.setShader(()->oldShader);
        }
    }
    @Override
    protected void beforeRender() {
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11C.GL_LESS);
        RenderSystem.depthMask(true);
    }

    @Override
    protected void afterRender(PoseStack poseStack) {
//        RenderSystem.enableCull();
        RenderSystem.depthMask(false);
        RenderSystem.depthFunc(GL11C.GL_ALWAYS); // 之后是渲染gui
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void buildBuffer(BufferBuilder buffer) {

    }

    @Override
    protected Supplier<ShaderInstance> setShader(){
        return ()->ModRenderTypes.Shaders.pixelStyleBlitShader;
    }

    @Override
    public BufferBuilder getBufferBuilder(){
        return new BufferBuilder(new ByteBufferBuilder(1536), VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        return Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    }

    @Override
    protected boolean shouldRefresh() {
        return false;
    }

    public static class NPCBufferSource implements MultiBufferSource {


        @Override
        public VertexConsumer getBuffer(RenderType renderType) {
            return null;
        }
    }
}
