package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

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
        super.setCustomAnimations(animatable, instanceId, animationState);
    }

    protected String getHead(){
        return "Head";
    }

}