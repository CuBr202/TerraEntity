package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade_lock.variant.BiomeLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.KillEntityLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.TimeLock;
import org.confluence.terraentity.registries.npc_trade_task.variant.*;

import java.util.function.Supplier;

/**
 * 注册交易任务编解码器的类型
 */
public class TradeLockProviderTypes {
    public static final DeferredRegister<TradeLockProvider> TYPES = DeferredRegister.create(TERegistries.TradeLockProviders.REGISTRY, TerraEntity.MODID);

    public static final Supplier<TradeLockProvider> BIOME_LOCK = register("biome_lock", BiomeLock.CODEC);
    public static final Supplier<TradeLockProvider> TIME_LOCK = register("time_lock", TimeLock.CODEC);
    public static final Supplier<TradeLockProvider> KILL_ENTITY_LOCK = register("kill_entity_lock", KillEntityLock.CODEC);



    public static Supplier<TradeLockProvider> register(String name,
                                                       MapCodec<? extends ITradeLock> codec) {
        return TYPES.register(name, ()->new TradeLockProvider(codec));
    }
}
