package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade_lock.variant.AndLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.NotLock;
import org.confluence.terraentity.registries.npc_trade_lock.variant.OrLock;

import java.util.Arrays;

/**
 * <h1>npc交易锁接口</h1>
 * <p>判断是否可以进行交易的接口</p>
 */
public interface ITradeLock {

    /**
     * <P>对交易进行额外的优先判断
     */
    boolean canTrade(Player player, ITradeHolder npc, int index);

    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeLockProvider getCodec();


    Codec<ITradeLock> TYPED_CODEC = TradeLockProviderTypes.REGISTRY.get()
                    .getCodec()
                    .dispatch(ITradeLock::getCodec, i->i.codec().codec());



    static ITradeLock and(ITradeLock... locks){
        return new AndLock(Arrays.asList(locks));
    }
    static ITradeLock or(ITradeLock... locks){
        return new OrLock(Arrays.asList(locks));
    }
    static ITradeLock not(ITradeLock lock){
        return new NotLock(lock);
    }
}
