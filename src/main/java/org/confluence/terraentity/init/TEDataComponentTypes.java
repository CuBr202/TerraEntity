package org.confluence.terraentity.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;

import java.util.function.Supplier;

public final class TEDataComponentTypes {
    public static final DeferredRegister.DataComponents TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TerraEntity.MODID);

    public static final Supplier<DataComponentType<EffectStrategyComponent>> EFFECT_STRATEGY = TYPES.registerComponentType(
            "effect_strategy", builder -> builder.persistent(EffectStrategyComponent.CODEC).networkSynchronized(EffectStrategyComponent.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<EffectStrategyComponent>> BOW_FULL_CHARGE_EFFECT_STRATEGY = TYPES.registerComponentType(
            "bow_full_charge_effect_strategy", builder -> builder.persistent(EffectStrategyComponent.CODEC).networkSynchronized(EffectStrategyComponent.STREAM_CODEC)
    );

}
