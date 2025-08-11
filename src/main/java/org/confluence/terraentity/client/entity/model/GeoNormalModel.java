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

    GeoBone head;
    String headName = "Head";
    private ResourceLocation path;

    public GeoNormalModel(ResourceLocation path) {
        this(path, true);
    }

    public GeoNormalModel(ResourceLocation path, boolean turnsHead) {
        super(path, turnsHead);
        this.path = path;
    }

    public GeoNormalModel(GeoNormalModel<T> model) {
        super(model.path, model.turnsHead);
        this.path = model.path;
        this.headName = model.headName;
        this.head = model.head;
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (this.turnsHead) {
            if (this.head == null){
                this.head = getHead();
            }
            if (this.head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                this.head.setRotX(entityData.headPitch() * 0.017453292F);
                this.head.setRotY(entityData.netHeadYaw() * 0.017453292F);
            }
        }
    }

    protected GeoBone getHead(){
        return this.getAnimationProcessor().getBone(getHeadName());
    }

    protected String getHeadName(){
        return headName;
    }

    public GeoNormalModel<T> setHeadName(String headName){
        this.headName = headName;
        return this;
    }

}