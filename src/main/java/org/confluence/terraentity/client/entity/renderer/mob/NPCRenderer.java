package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.util.function.Consumer;

public class NPCRenderer<T extends AbstractTerraNPC> extends HumanoidRenderer<T>{

    // 应该使用static，在所有实体渲染完后再bind
    MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
    VertexBuffer consumeBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    TextureTarget target = new TextureTarget(100,100,false, false);

    public NPCRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path.withPrefix("npc/"));
    }

    @Override
    public float getMotionAnimThreshold(T animatable) {
        return 0.01F;
    }



    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
                               MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
                               int packedLight, int packedOverlay, int colour) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        if(Minecraft.getInstance().player != null && false) {
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
//            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);

            target.setClearColor(0,1,0,0);
            target.clear(true);
            target.bindWrite(true);

            poseStack.pushPose();

            poseStack.translate(0, 2.5, 0);


            // 离屏渲染聊天气泡

            Matrix4f cache_m = new Matrix4f(RenderSystem.getModelViewMatrix());
            Matrix4f cache_p = new Matrix4f(RenderSystem.getProjectionMatrix());
            RenderSystem.getModelViewMatrix().set(new Matrix4f());
            RenderSystem.getProjectionMatrix().set(createOrthographicMatrix(0, 100, 100, 0, -100, 100));

            GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), this.bufferSource);

            guiGraphics.drawString(Minecraft.getInstance().font, "hello", 40,40,0xffffffff);
            guiGraphics.blit(TerraEntity.space("textures/gui/chat_bubble.png"), 0,0,100,100,0,0,100,100,100, 100);


            guiGraphics.flush();

            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);

            RenderSystem.getModelViewMatrix().set(cache_m);
            RenderSystem.getProjectionMatrix().set(cache_p);

            target.blitToScreen(200,200);

            // 面向摄像机
            float yaw = -Mth.lerp(partialTick, Minecraft.getInstance().player.yHeadRotO, Minecraft.getInstance().player.yHeadRot);
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));


            // 广告牌渲染到头顶
            renderChatBubble(poseStack.last().pose(), shader->{
                RenderSystem.setShaderTexture(0, target.getColorTextureId());
            });


            poseStack.popPose();
        }
    }

    /**
     * 渲染聊天气泡
     */
    private void renderChatBubble(Matrix4f viewMatrix, Consumer<ShaderInstance> setSampler ) {
        if(consumeBuffer!= null){
            consumeBuffer.close();
        }

        BufferBuilder consumer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        consumer.addVertex(viewMatrix ,-0.5f,-0.5f,0).setUv(1,0);
        consumer.addVertex(viewMatrix ,0.5f,-0.5f,0).setUv(0,0);
        consumer.addVertex(viewMatrix ,0.5f,0.5f,0).setUv(0,1);
        consumer.addVertex(viewMatrix ,-0.5f,0.5f,0).setUv(1,1);

        var build = consumer.build();
        if(build == null){
            consumeBuffer = null;
        } else{
            consumeBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.setShaderTexture(0, texture);
            setSampler.accept(GameRenderer.getPositionTexShader());


            consumeBuffer.bind();
            consumeBuffer.upload(build);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();

            if (Minecraft.getInstance().player != null) {
                consumeBuffer.drawWithShader(
                        new Matrix4f()
                                .rotate(new Quaternionf(Minecraft.getInstance().gameRenderer.getMainCamera()
                                        .rotation())
                                .conjugate()),
                        RenderSystem.getProjectionMatrix(),
                        GameRenderer.getPositionTexShader());
            }

            VertexBuffer.unbind();
            RenderSystem.enableCull();
        }
    }

    private void renderChatBubble(Matrix4f viewMatrix, ResourceLocation texture ) {
        this.renderChatBubble(viewMatrix, (shader) -> {
            RenderSystem.setShaderTexture(0, texture);
        });
    }

    public static Matrix4f createOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.setOrtho(left, right, bottom, top, near, far);
        return matrix;
    }
}
