package org.confluence.terraentity.client.boss.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.entity.boss.Skeletron;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class SkeletronRenderer extends GeoBossRenderer<Skeletron,GeoBossModel<Skeletron>> {

    public SkeletronRenderer(EntityRendererProvider.Context renderManager, GeoBossModel<Skeletron> model) {
        super(renderManager, model);
    }

    @Override
    public void preRender(PoseStack poseStack, Skeletron animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        model.getBone("bone3").ifPresent(bone -> bone.setHidden(true));
//        poseStack.scale(0.3f, 0.3f, 0.3f);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

}
