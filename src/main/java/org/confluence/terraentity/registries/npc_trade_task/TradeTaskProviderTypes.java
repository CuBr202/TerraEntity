package org.confluence.terraentity.registries.npc_trade_task;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.variant.TradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.AnglerTradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.ProgressTradeTask;
import org.confluence.terraentity.registries.npc_trade_task.variant.RandomTradeTask;

import java.util.function.Supplier;

/**
 * 注册交易任务编解码器的类型
 */
public class TradeTaskProviderTypes {
    public static final DeferredRegister<TradeTaskProvider> TYPES = DeferredRegister.create(TERegistries.TradeTaskProviders.REGISTRY, TerraEntity.MODID);

    public static final Supplier<TradeTaskProvider> PROGRESS_TRADE_TASK = register("progress_trade_task", ProgressTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> RANDOM_TRADE_TASK = register("random_trade_task", RandomTradeTask.CODEC);
    public static final Supplier<TradeTaskProvider> ANGLER_TRADE_TASK = register("angler_trade_task", AnglerTradeTask.CODEC);


    public static Supplier<TradeTaskProvider> register(String name,
                                                       MapCodec<? extends ITradeTask> codec) {
        return TYPES.register(name, ()->new TradeTaskProvider(codec));
    }
}
