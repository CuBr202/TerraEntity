package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;

import java.util.function.Supplier;

public final class TEDataComponentTypes {
    public static final DeferredRegister<DataComponentProvider<? extends IDataComponentType<?>>> TYPES =  DeferredRegister.create(TERegistries.DataComponentProviders.REGISTRY.get(), TerraEntity.MODID);


    public static final Supplier<DataComponentProvider<EffectStrategyComponent>> EFFECT_STRATEGY =
            register("effect_strategy", EffectStrategyComponent.CODEC);

    public static <T extends IDataComponentType<T>> Supplier<DataComponentProvider<T>> register(String name, Supplier<Codec<T>> codec) {
        return TERegistries.DataComponentProviders.TYPES.register(name, () -> new DataComponentProvider<>(name, codec));
    }

}
