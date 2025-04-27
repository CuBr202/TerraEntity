package org.confluence.terraentity.registries.npc_trade_list;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade_list.variant.SimpleGenerator;
import org.confluence.terraentity.registries.npc_trade_list.variant.WeightMapGenerator;

import java.util.function.Supplier;

/**
 * 注册交易表生成器编解码器的类型
 */
public class TradeGeneratorProviderTypes {
    public static final DeferredRegister<TradeGeneratorProvider> TYPES = DeferredRegister.create(TERegistries.TradeGeneratorProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<TradeGeneratorProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<TradeGeneratorProvider> SIMPLE_LIST = register("simple_list", ()->SimpleGenerator.CODEC);
    public static final Supplier<TradeGeneratorProvider> WEIGHT_MAP = register("weight_map", ()->WeightMapGenerator.CODEC);



    public static Supplier<TradeGeneratorProvider> register(String name,
                                                            Supplier<MapCodec<? extends ITradeGenerator>> codec) {
        return TYPES.register(name, ()->new TradeGeneratorProvider(codec));
    }
}
