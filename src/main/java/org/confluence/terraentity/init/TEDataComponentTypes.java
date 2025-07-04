package org.confluence.terraentity.init;

import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.github.edg_thexu.cafelib.data.component.SingleBooleanComponent;
import com.github.edg_thexu.cafelib.registries.CafeLibRegistries;
import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;

import java.util.function.Supplier;

public final class TEDataComponentTypes {

    public static DeferredRegister<DataComponentProvider<? extends IDataComponentType<?>>> TYPES =  DeferredRegister.create(CafeLibRegistries.DataComponentProviders.KEY, TerraEntity.MODID);
//    public static final Supplier<IForgeRegistry<DataComponentProvider<?>>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);


    public static final Supplier<DataComponentProvider<EffectStrategyComponent>> EFFECT_STRATEGY =
            register("effect_strategy", EffectStrategyComponent.CODEC);

    public static final Supplier<DataComponentProvider<EffectStrategyComponent>> EFFECT_STRATEGY_BENEFICIAL =
            register("effect_strategy_beneficial", EffectStrategyComponent.CODEC);

    public static final Supplier<DataComponentProvider<SingleBooleanComponent>> BOOMERANG_READY =
            register("boomerang_ready", SingleBooleanComponent.CODEC);



    private static <T extends IDataComponentType<T>> Supplier<DataComponentProvider<T>> register(String location, Supplier<Codec<T>> codec) {
        return TYPES.register(location, () -> new DataComponentProvider<>(TerraEntity.MODID + ":"+location, codec));
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

}
