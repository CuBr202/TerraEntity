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
import org.confluence.terraentity.api.npc.chat.IBubbleRenderer;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.config.ClientConfig;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.chat.ChatArranger;
import org.confluence.terraentity.mixed.IShaderInstance;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.util.function.Supplier;

public class NPCRenderer<T extends AbstractTerraNPC> extends HumanoidRenderer<T>{

    // 应该使用static，在所有实体渲染完后再bind
    MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
    VertexBuffer consumeBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    TextureTarget target = new TextureTarget(200,200,false, false);

//    IBubbleRenderer bubbleRenderer = CloudBubble.INSTANCE;
    IBubbleRenderer bubbleRenderer;

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

        if(Minecraft.getInstance().player != null && animatable.getChat()!= null) {
            ChatArranger chat = animatable.getChat();
            float scale = chat.getBubbleScale();
            float width = 50 * scale;
            float height = 50 * scale;
            float progress = (animatable.chatCount - partialTick )/ animatable._chatCount;
            progress = progress * (1 - progress) * 10;
            progress = 1 - Mth.clamp(progress, 0, 1);


            // 存储之前的mp
            Matrix4f cache_m = new Matrix4f(RenderSystem.getModelViewMatrix());
            Matrix4f cache_p = new Matrix4f(RenderSystem.getProjectionMatrix());
            RenderSystem.getModelViewMatrix().set(new Matrix4f());
            RenderSystem.getProjectionMatrix().set(createOrthographicMatrix(0, 100 * scale, 100 * scale, 0, -100, 100));

            // 离屏渲染聊天气泡
            target.setClearColor(0,1,0,0f);
            target.clear(true);
            target.bindWrite(true);
            GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), this.bufferSource);


            // 渲染阴影
            bubbleRenderer = ClientConfig.NPC_CHAT_BUBBLE_STYLE.get().getRenderer();
            bubbleRenderer.renderBubble(guiGraphics, chat, poseStack, animatable.level(), this.bufferSource, scale, width, height, packedLight, packedOverlay);

            guiGraphics.flush();

                // 恢复之前的mp
            RenderSystem.getModelViewMatrix().set(cache_m);
            RenderSystem.getProjectionMatrix().set(cache_p);

            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
//            target.blitToScreen(200,200); // debug

            poseStack.pushPose();
            poseStack.translate(0, 2.5, 0);

            // yaw面向摄像机
            float yaw = -Mth.lerp(partialTick, Minecraft.getInstance().player.yHeadRotO, Minecraft.getInstance().player.yHeadRot);
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

            bubbleRenderer.adjustPose(poseStack, scale, width, height);


            // 聊天气泡广告牌渲染到头顶
            poseStack.scale(scale, scale, 1);
            poseStack.translate(0, height / 50 * 0.05f, 0);
            float finalProgress = progress;
            renderChatBubble(poseStack.last().pose(), ()->{
                RenderSystem.setShaderTexture(0, target.getColorTextureId());
                RenderSystem.setShaderTexture(1, TerraEntity.space("textures/gui/noise.png"));
                IShaderInstance shader = (IShaderInstance) ModRenderTypes.Shaders.pixelStyleBlitShader;
                shader.getTerra_entity$Progress().set(finalProgress);
                shader.getTerra_entity$PixelSize().set(32f);
                return ModRenderTypes.Shaders.pixelStyleBlitShader;
            });

            poseStack.popPose();
        }
    }

//    private void renderBubble(GuiGraphics guiGraphics, ChatArranger chat, PoseStack poseStack, Level level, MultiBufferSource.BufferSource bufferSource, float scale, float width, float height , int packedLight, int packedOverlay) {
//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().scale(scale, scale, 1);
//        guiGraphics.setColor(0.3f,0.3f,0.3f,0.2f);
//        guiGraphics.blit(TerraEntity.space("textures/gui/chat_bubble.png"), 1,1,100,100,0,0,100,100,100, 100);
//        guiGraphics.setColor(1,1,1,1);
//        guiGraphics.blit(TerraEntity.space("textures/gui/chat_bubble.png"), 0,0,100,100,0,0,100,100,100, 100);
//        guiGraphics.pose().popPose();
//
//        Map<IChatElement, Vec2> elements = chat.elementPositions;
//        for (var element: elements.entrySet()) {
//            Vec2 pos = element.getValue();
//            if(element.getKey().getRenderer() != null){
//                element.getKey().getRenderer().render(element.getKey().getContent(), width + pos.x, height - 6  + pos.y, poseStack, guiGraphics, packedLight, packedOverlay, level, bufferSource);
//            }
//
//        }
//    }

    /**
     * 渲染聊天气泡Billboard
     */
    private void renderChatBubble(Matrix4f viewMatrix, Supplier<ShaderInstance> setSampler ) {
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
            RenderSystem.setShader(setSampler);
//            RenderSystem.setShaderTexture(0, texture);
//            setSampler.accept(GameRenderer.getPositionTexShader());


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
                        RenderSystem.getShader());
            }

            VertexBuffer.unbind();
            RenderSystem.enableCull();
        }
    }

    private void renderChatBubble(Matrix4f viewMatrix, ResourceLocation texture) {
        this.renderChatBubble(viewMatrix, () -> {
            RenderSystem.setShaderTexture(0, texture);
            return GameRenderer.getPositionTexShader();
        });
    }

    /**
     * 创建正交投影矩阵
     */
    public static Matrix4f createOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.setOrtho(left, right, bottom, top, near, far);
        return matrix;
    }

}
