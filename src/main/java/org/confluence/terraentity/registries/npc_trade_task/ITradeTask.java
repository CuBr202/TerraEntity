package org.confluence.terraentity.registries.npc_trade_task;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.ITrade;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <h1>npc交易任务接口</h1>
 */
public interface ITradeTask {

    @Nullable
    ITrade getSelected(AbstractTerraNPC npc);

    void setNext(AbstractTerraNPC npc);

    boolean canTrade(AbstractTerraNPC npc);

    default void onTrade(AbstractTerraNPC npc, ITrade trade) {
        setNext(npc);
    }
    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeTaskProvider getCodec();


    Codec<ITradeTask> TYPED_CODEC = TERegistries.TradeTaskProviders.REGISTRY
            .byNameCodec()
            .dispatch(ITradeTask::getCodec, TradeTaskProvider::codec);

    StreamCodec<ByteBuf, ITradeTask> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);

    StreamCodec<ByteBuf, List<ITradeTask>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

}
