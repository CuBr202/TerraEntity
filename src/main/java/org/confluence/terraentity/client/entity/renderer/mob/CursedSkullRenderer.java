package org.confluence.terraentity.client.entity.renderer.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.monster.CursedSkull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CursedSkullRenderer<T extends CursedSkull> extends GeoNormalRenderer<T> {
    public CursedSkullRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        this(renderManager, path, true);
    }

    public CursedSkullRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, boolean ifRotX) {
        this(renderManager, path, ifRotX, 1.0F, 0.0F);
    }

    public CursedSkullRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path, boolean ifRotX, float scale, float offsetY) {
        this(renderManager, new GeoNormalModel<>(path), ifRotX, scale, offsetY);
    }

    public CursedSkullRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, boolean ifRotX, float scale, float offsetY) {
        super(renderManager, model, ifRotX, scale, offsetY);

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){

            @Override
            protected RenderType getRenderType(T animatable) {
                return RenderType.eyes(getTextureResource(animatable));
            }
        });
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        model.getBone("outline").ifPresent(bone -> {
            bone.setHidden(!isReRender);
        });
        model.getBone("bone").ifPresent(bone -> {
            bone.setHidden(isReRender);
        });
        if(isReRender){
            return;
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, r, g, b, a);

    }

    @Override
    public  void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
                                MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
                                int packedLight, int packedOverlay, float r, float g, float b, float a) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight | 0x00000080, packedOverlay, r, g, b, a);
    }
}
