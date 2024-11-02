package org.confluence.terraentity.client.entity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import org.confluence.terraentity.TerraEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GeoNormalModel<T extends GeoEntity> extends DefaultedEntityGeoModel<T> {
    public GeoNormalModel(String path) {
        super(ResourceLocation.fromNamespaceAndPath(TerraEntity.MODID, path));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}