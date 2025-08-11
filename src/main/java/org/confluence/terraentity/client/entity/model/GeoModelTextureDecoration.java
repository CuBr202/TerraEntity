package org.confluence.terraentity.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;

/**
 * 图片路径装饰器
 */
public class GeoModelTextureDecoration<T extends GeoEntity> extends GeoNormalModel<T> {

    ResourceLocation texture;
    public GeoModelTextureDecoration(GeoNormalModel<T> model, ResourceLocation basePath) {
        super(model);
        this.texture = this.buildFormattedTexturePath(basePath);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return this.texture;
    }
}