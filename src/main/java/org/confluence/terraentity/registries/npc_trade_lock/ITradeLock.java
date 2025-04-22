package org.confluence.terraentity.registries.npc_trade_lock;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;

import java.util.function.Supplier;

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

    class lazy{
        static Codec<ITradeLock> lazy = null;
    }

    Codec<ITradeLock> TYPED_CODEC = TradeLockProviderTypes.REGISTRY.get()
                    .getCodec()
                    .dispatch(ITradeLock::getCodec, i->i.codec().codec());


//    Supplier<Codec<ITradeLock>> TYPED_CODEC = ()->{
//        if(lazy.lazy == null){
//            var registry = TradeLockProviderTypes.REGISTRY.get();
//            if(registry == null){
//                return null;
//            }
//            lazy.lazy = TradeLockProviderTypes.REGISTRY.get()
//                    .getCodec()
//                    .dispatch(ITradeLock::getCodec, i->i.codec().codec());
//        }
//        return lazy.lazy;
//    };


//    StreamCodec<ByteBuf, ITradeLock> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);
}
