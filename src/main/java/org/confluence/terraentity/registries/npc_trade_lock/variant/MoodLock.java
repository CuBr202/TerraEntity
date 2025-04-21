package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

import java.util.Optional;

/**
 * 心情值锁
 * <p>仅当holder为npc有效
 * @param value 最小/最大心情值
 * @param reverse 默认为false。当为true时且mood小于value可交易
 */
public record MoodLock(int value, boolean reverse) implements ITradeLock {

    public static final MapCodec<MoodLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("from").forGetter(MoodLock::value),
            Codec.BOOL.optionalFieldOf("exclude").forGetter(i-> Optional.of(i.reverse))
    ).apply(instance, (from, exclude)->new MoodLock(from, exclude.orElse(false))));

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        if(npc instanceof AbstractTerraNPC terraNPC){
            int mood = terraNPC.getMood().getValue();
            return (reverse? mood < value : mood > value);
        }
        return false;
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.MOOD_LOCK.get();
    }
}
