package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.track.TrackTypeProvider;
import org.confluence.terraentity.registries.track.variant.SimpleTrack;

import java.util.List;

/**
 * <h1>跟踪方式</h1>
 */
public interface ITrade{

    boolean canTrade(Player player);

    ItemStack result();

    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeProvider getCodec();


    MapCodec<ITrade> TYPED_CODEC = TradeProviderTypes.REGISTRY.get()
            .getCodec()
            .dispatchMap(ITrade::getCodec, i->i.codec().codec());

//    StreamCodec<ByteBuf, ITrade> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);

//    StreamCodec<ByteBuf, List<ITrade>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

}
