package org.confluence.terraentity.registries.npc_trade_task;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.npc.trade.ITradeTask;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade_task.variant.*;

import java.util.function.Supplier;

/**
 * 注册交易任务编解码器的类型
 */
public class TradeTaskProviderTypes {
    public static final DeferredRegister<TradeTaskProvider> TYPES = DeferredRegister.create(TERegistries.Keys.TRADE_TASK_PROVIDER, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<TradeTaskProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<TradeTaskProvider> PROGRESS_TRADE_TASK = register("progress_trade_task", ()->ProgressTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> RANDOM_TRADE_TASK = register("random_trade_task", ()->RandomTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> ANGLER_TRADE_TASK = register("fixed_map_trade_task", ()->FixedMapTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> DYNAMIC_POOL_MAP_TRADE_TASK = register("dynamic_pool_map_trade_task", ()->DynamicPoolTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> DYNAMIC_ANGLER_TRADE_TASK = register("dynamic_angler_trade_task", ()->DynamicAnglerTradeTask.CODEC);


    public static Supplier<TradeTaskProvider> register(String name,
                                                       Supplier<MapCodec<? extends ITradeTask>> codec) {
        return TYPES.register(name, ()->new TradeTaskProvider(codec));
    }
}
