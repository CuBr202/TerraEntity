package org.confluence.terraentity.client.boss.model;

import net.minecraft.resources.ResourceLocation;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.boss.CthulhuEye;
import software.bernie.geckolib.model.GeoModel;

public class CthulhuEyeModel extends GeoModel<CthulhuEye> {

    private static final String name = "eye_of_cthulhu";

    private static final ResourceLocation MODEL = TerraEntity.space("geo/entity/boss/" + name + ".geo.json");
    private static final ResourceLocation TEXTURES = TerraEntity.space("textures/entity/boss/" + name + ".png");
    private static final ResourceLocation ANIMATION = TerraEntity.space("animations/entity/boss/" + name + ".animation.json");

    @Override
    public ResourceLocation getModelResource(CthulhuEye animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CthulhuEye animatable) {
        return TEXTURES;
    }

    @Override
    public ResourceLocation getAnimationResource(CthulhuEye animatable) {
        return ANIMATION;
    }
}
