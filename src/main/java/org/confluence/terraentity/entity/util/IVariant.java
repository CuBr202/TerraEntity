package org.confluence.terraentity.entity.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.VariantHolder;

import java.util.Map;

public interface IVariant<T> {

    default ResourceLocation getTexture(){
        return getTexturesMap().get(getTEVariant());
    }

    Map<T, ResourceLocation> getTexturesMap();

    T getTEVariant();

    void setTEVariant(T variant);
}
