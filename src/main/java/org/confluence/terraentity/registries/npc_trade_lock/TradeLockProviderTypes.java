package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.api.npc.trade.ITradeLock;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade_lock.variant.*;

import java.util.function.Supplier;

/**
 * 注册交易任务编解码器的类型
 */
public class TradeLockProviderTypes {
    public static final DeferredRegister<TradeLockProvider> TYPES = DeferredRegister.create(TERegistries.TRADE_LOCK_PROVIDERS, TerraEntity.MODID);

    public static final Supplier<TradeLockProvider> AND_LOCK = register("and_lock", AndLock.CODEC);
    public static final Supplier<TradeLockProvider> OR_LOCK = register("or_lock", OrLock.CODEC);
    public static final Supplier<TradeLockProvider> NOT_LOCK = register("not_lock", NotLock.CODEC);
    public static final Supplier<TradeLockProvider> TRUE_LOCK = register("true_lock", TrueLock.CODEC);

    public static final Supplier<TradeLockProvider> BIOME_LOCK = register("biome_lock", BiomeLock.CODEC);
    public static final Supplier<TradeLockProvider> TIME_LOCK = register("time_lock", TimeLock.CODEC);
    public static final Supplier<TradeLockProvider> KILL_ENTITY_LOCK = register("kill_entity_lock", KillEntityLock.CODEC);
    public static final Supplier<TradeLockProvider> MOOD_LOCK = register("mood_lock", MoodLock.CODEC);
    public static final Supplier<TradeLockProvider> NPC_EXIST_LOCK = register("npc_exist_lock", NPCExistLock.CODEC);



    public static Supplier<TradeLockProvider> register(String name,
                                                       MapCodec<? extends ITradeLock> codec) {
        return TYPES.register(name, ()->new TradeLockProvider(codec));
    }
}
