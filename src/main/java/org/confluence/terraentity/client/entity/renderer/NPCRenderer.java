package org.confluence.terraentity.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;

public class NPCRenderer<T extends AbstractTerraNPC> extends HumanoidRenderer<T>{
    public NPCRenderer(EntityRendererProvider.Context renderManager, ResourceLocation path) {
        super(renderManager, path.withPrefix("npc/"));
    }
}
