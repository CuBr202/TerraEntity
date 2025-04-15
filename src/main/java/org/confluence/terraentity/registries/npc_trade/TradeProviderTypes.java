package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.variant.ItemListTradeItem;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeHealth;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;

import java.util.List;
import java.util.function.Supplier;

/**
 * 注册追踪编解码器的类型
 */
public class TradeProviderTypes {
    public static final DeferredRegister<TradeProvider> TYPES = DeferredRegister.create(TERegistries.TradeProviders.REGISTRY, TerraEntity.MODID);

    public static final Supplier<TradeProvider> ITEM_TRADE_ITEM = register("item_trade_item", ItemTradeItem.CODEC);
    public static final Supplier<TradeProvider> ITEM_TRADE_HEALTH = register("item_trade_health", ItemTradeHealth.CODEC);
    public static final Supplier<TradeProvider> ITEM_LIST_TRADE_ITEM = register("item_list_trade_health", ItemListTradeItem.CODEC);


    public static Supplier<TradeProvider> register(String name,
                                                    MapCodec<? extends ITrade> codec) {
        return TYPES.register(name, ()->new TradeProvider(codec));
    }
}
