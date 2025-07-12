package org.confluence.terraentity.entity.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.VariantHolder;

public interface IVanillaVariant<T> extends IVariant<T> , VariantHolder<T> {

    default ResourceLocation getTexture(){
        return getTexturesMap().get(getVariant());
    }

    default T getTEVariant(){
        return getVariant();
    }

    default void setTEVariant(T variant){
        setVariant(variant);
    }
}
