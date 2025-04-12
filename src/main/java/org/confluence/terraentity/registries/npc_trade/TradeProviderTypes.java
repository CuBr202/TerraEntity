package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItem;

import java.util.function.Supplier;

/**
 * 注册追踪编解码器的类型
 */
public class TradeProviderTypes {
    public static final DeferredRegister<TradeProvider> TYPES = DeferredRegister.create(TERegistries.TradeProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<TradeProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<TradeProvider> ITEM_TRADE_ITEM = register("item_trade_item", ItemTradeItem.CODEC);


    public static Supplier<TradeProvider> register(String name,
                                                   MapCodec<? extends ITrade> codec) {
        return TYPES.register(name, ()->new TradeProvider(codec));
    }
}
