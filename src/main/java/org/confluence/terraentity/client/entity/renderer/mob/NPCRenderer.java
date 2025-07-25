package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class NPCRenderer<T extends AbstractTerraNPC> extends HumanoidRenderer<T>{
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
        if(Minecraft.getInstance().player != null) {
            poseStack.pushPose();

            poseStack.translate(0, 2.5, 0);

            float yaw = -Mth.lerp(partialTick, Minecraft.getInstance().player.yHeadRotO, Minecraft.getInstance().player.yHeadRot);
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.scale(0.25f, 0.25f, 0.25f);


            Minecraft.getInstance().getItemRenderer().renderStatic(animatable, Items.BOW.getDefaultInstance(),
                    ItemDisplayContext.GUI, false, poseStack, bufferSource, animatable.level(), packedLight, packedOverlay, 0);

            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.scale(0.1f, 0.1f, 0.1f);
            Minecraft.getInstance().font.drawInBatch("hello", 0,0,0xffffffff,false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0x00000000, 0x0F000F0);

            poseStack.popPose();
        }

    }
}
