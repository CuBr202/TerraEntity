package org.confluence.terraentity.entity.monster;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.VariantHolder;

import java.util.Map;

public interface IVariant<T> extends VariantHolder<T> {
    default ResourceLocation getTexture(){
        return getTexturesMap().get(this.getVariant());
    }

    Map<T, ResourceLocation> getTexturesMap();
}
