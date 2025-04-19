package org.confluence.terraentity.registries.npc_trade;

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

import java.util.List;

/**
 * <h1>npc交易接口</h1>
 */
public interface ITrade{

    /**
     * 能否触发onTrade
     */
    boolean canTrade(Player player, AbstractTerraNPC npc, int index);

    /**
     * 当canTrade为true时触发
     */
    void onTrade(ServerPlayer player, AbstractTerraNPC npc, int index);

    /**
     * 渲染框内的所需物品
     * @param guiGraphics guiGraphics
     * @param font font
     * @param x 当前绘制位置x
     * @param y 当前绘制位置y
     * @param startx 菜单左上角位置x
     * @param starty 菜单左上角位置y
     */
    @OnlyIn(Dist.CLIENT)
    void renderCosts(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY);

    /**
     * 渲染交易列表的表格调用
     */
    @OnlyIn(Dist.CLIENT)
    void renderResult(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, int slotIndex);

    /**
     * 悬浮于交易列表物品上调用，只会在悬浮于交易项时调用一次
     */
    @OnlyIn(Dist.CLIENT)
    void renderResultHover(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY);

    /**
     * 渲染交易列表的物品槽调用
     */
    @OnlyIn(Dist.CLIENT)
    void renderResultSlot(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY, boolean canBuy, Slot slot);

    /**
     * 当客户端点击物品槽时调用，自定义播放声音
     */
    default void onLocalClickSlot(Player player, int button, ClickType clickType, AbstractTerraNPC npc, int index){
        if(canTrade(player, npc, index)){
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        }else{
            player.playSound(SoundEvents.UI_TOAST_IN);
        }
    }


    /**
     * 获取编解码器
     * @return 编解码器
     */
    TradeProvider getCodec();


    Codec<ITrade> TYPED_CODEC = TERegistries.TradeProviders.REGISTRY
            .byNameCodec()
            .dispatch(ITrade::getCodec, TradeProvider::codec);

    StreamCodec<ByteBuf, ITrade> STREAM_CODEC = ByteBufCodecs.fromCodec(TYPED_CODEC);

    StreamCodec<ByteBuf, List<ITrade>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

}
