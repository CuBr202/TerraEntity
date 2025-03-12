package org.confluence.terraentity.client.boss.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.confluence.terraentity.entity.boss.Skeletron;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SkeletronRenderer extends GeoEntityRenderer<Skeletron> {

    public SkeletronRenderer(EntityRendererProvider.Context renderManager, GeoModel<Skeletron> model) {
        super(renderManager, model);
    }
}
