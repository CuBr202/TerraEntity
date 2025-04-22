package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade_lock.variant.BiomeLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.KillEntityLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.MoodLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.TimeLock;

import java.util.function.Supplier;

/**
 * 注册交易任务编解码器的类型
 */
public class TradeLockProviderTypes {
    public static final DeferredRegister<TradeLockProvider> TYPES = DeferredRegister.create(TERegistries.TradeLockProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<TradeLockProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<TradeLockProvider> BIOME_LOCK = register("biome_lock", ()->BiomeLock.CODEC);
    public static final Supplier<TradeLockProvider> TIME_LOCK = register("time_lock", ()->TimeLock.CODEC);
    public static final Supplier<TradeLockProvider> KILL_ENTITY_LOCK = register("kill_entity_lock", ()->KillEntityLock.CODEC);
    public static final Supplier<TradeLockProvider> MOOD_LOCK = register("mood_lock", ()->MoodLock.CODEC);



    public static Supplier<TradeLockProvider> register(String name,
                                                       Supplier<MapCodec<? extends ITradeLock>> codec) {
        return TYPES.register(name, ()->new TradeLockProvider(codec));
    }
}
