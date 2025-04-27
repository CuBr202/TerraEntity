package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GeoNormalModel<T extends GeoEntity> extends DefaultedEntityGeoModel<T> {

    public GeoNormalModel(ResourceLocation path) {
        super(path, true);
    }

    public GeoNormalModel(ResourceLocation path, boolean turnsHead) {
        super(path, turnsHead);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (this.turnsHead) {
            GeoBone head = this.getAnimationProcessor().getBone(getHead());
            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                head.setRotX(entityData.headPitch() * 0.017453292F);
                head.setRotY(entityData.netHeadYaw() * 0.017453292F);
            }
        }
    }

    protected String getHead(){
        return "Head";
    }

}