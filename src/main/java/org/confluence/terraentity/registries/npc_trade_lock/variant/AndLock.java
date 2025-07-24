package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.api.trade.ITradeHolder;
import org.confluence.terraentity.api.trade.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

import java.util.List;

/**
 * 与锁
 * @param locks
 *
 */
public record AndLock(List<ITradeLock> locks) implements ITradeLock  {

    public static final MapCodec<AndLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ITradeLock.TYPED_CODEC.listOf().fieldOf("locks").forGetter(AndLock::locks)
    ).apply(instance, AndLock::new));

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        return locks.stream().allMatch(lock -> lock.canTrade(player, npc, index));
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.AND_LOCK.get();
    }
}
