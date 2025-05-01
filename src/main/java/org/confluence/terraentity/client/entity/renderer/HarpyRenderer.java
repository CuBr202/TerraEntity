package org.confluence.terraentity.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.confluence.terraentity.client.entity.model.AnimatorModel;
import org.confluence.terraentity.entity.monster.Harpy;

public class HarpyRenderer extends AnimatorRenderer<Harpy> {


    public HarpyRenderer(EntityRendererProvider.Context renderManager, AnimatorModel<Harpy> model, boolean ifRotX, float scale, float offsetY) {
        super(renderManager, model, ifRotX, scale, offsetY);
    }
}
