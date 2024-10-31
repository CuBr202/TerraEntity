package org.confluence.terraentity.client.entity.renderer;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.entity.monster.BloodySpore;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloodySporeRenderer extends GeoEntityRenderer<BloodySpore> {

    public BloodySporeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoNormalModel<>("bloody_spore"));
    }

    @Override
    protected float getDeathMaxRotation(BloodySpore animatable){
        return 0;
    }
}
