package org.confluence.terraentity.registries.npc_trade_lock.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProvider;
import org.confluence.terraentity.registries.npc_trade_lock.TradeLockProviderTypes;

public record TimeLock(int from, int to, boolean exclude) implements ITradeLock  {

    public static final MapCodec<TimeLock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("from").forGetter(TimeLock::from),
            Codec.INT.fieldOf("to").forGetter(TimeLock::to),
            Codec.BOOL.fieldOf("exclude").forGetter(TimeLock::exclude)
    ).apply(instance, TimeLock::new));

    @Override
    public boolean canTrade(Player player, ITradeHolder npc, int index) {
        int dayTime = (int) (npc.level().dayTime() % 24000);
        if(exclude){
            return dayTime < from || dayTime > to;
        }
        return dayTime >= from && dayTime < to ;
    }

    @Override
    public TradeLockProvider getCodec() {
        return TradeLockProviderTypes.TIME_LOCK.get();
    }
}
