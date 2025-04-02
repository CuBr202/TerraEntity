package org.confluence.terraentity.client.boss.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.monster.Decayeder;

public class DecayederRenderer extends SkeletonRenderer {
    private static final ResourceLocation SKELETON_LOCATION = TerraEntity.space("textures/entity/decayeder.png");


    public DecayederRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(Decayeder entity) {
        return SKELETON_LOCATION;
    }

}
