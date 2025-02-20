package org.confluence.terraentity.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.monster.IVariant;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class GiantShellyModel<T extends Entity & IVariant<Integer> & GeoEntity> extends GeoModel<T> {
    private final ResourceLocation animation;
    private final ResourceLocation model;

    public GiantShellyModel(ResourceLocation path) {
        String path_name = path.getPath();
        animation = TerraEntity.space("animations/entity/" + path_name + ".animation.json");
        model = TerraEntity.space("geo/entity/" + path_name + ".geo.json");
    }

    @Override
    public ResourceLocation getModelResource(T entity) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return entity.getTexture();
    }
    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return animation;
    }
}
