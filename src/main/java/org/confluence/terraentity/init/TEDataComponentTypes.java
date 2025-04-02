package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.confluence.terraentity.data.component.Unbreakable;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.datacomponent.DataComponentProvider;
import org.confluence.terraentity.registries.datacomponent.IDataComponentType;

import java.util.function.Supplier;

public final class TEDataComponentTypes {

    public static DeferredRegister<DataComponentProvider<? extends IDataComponentType<?>>> TYPES =  DeferredRegister.create(TERegistries.DataComponentProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<DataComponentProvider<?>>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);


    public static final Supplier<DataComponentProvider<EffectStrategyComponent>> EFFECT_STRATEGY =
            register("effect_strategy", EffectStrategyComponent.CODEC);

    public static final Supplier<DataComponentProvider<EffectStrategyComponent>> EFFECT_STRATEGY_BENEFICIAL =
            register("effect_strategy_beneficial", EffectStrategyComponent.CODEC);


    public static final Supplier<DataComponentProvider<Unbreakable>> UNBREAKABLE =
            register("unbreakable", Unbreakable.CODEC);


    public static final Supplier<DataComponentProvider<SingleBooleanComponent>> BOOMERANG_READY =
            register("boomerang_ready", SingleBooleanComponent.CODEC);





    private static <T extends IDataComponentType<T>> Supplier<DataComponentProvider<T>> register(String location, Supplier<Codec<T>> codec) {
        return TYPES.register(location, () -> new DataComponentProvider<>(TerraEntity.MODID + ":"+location, codec));
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

}
