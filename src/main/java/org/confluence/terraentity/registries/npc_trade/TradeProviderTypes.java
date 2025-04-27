package org.confluence.terraentity.registries.npc_trade;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.TERegistries;
import org.confluence.terraentity.registries.npc_trade.variant.*;

import java.util.function.Supplier;

/**
 * 注册交易编解码器的类型
 */
public class TradeProviderTypes {
    public static final DeferredRegister<TradeProvider> TYPES = DeferredRegister.create(TERegistries.TradeProviders.KEY, TerraEntity.MODID);
    public static final Supplier<IForgeRegistry<TradeProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<TradeProvider> ITEM_TRADE_HEALTH = register("ingredient_trade_health", ()->ItemTradeHealth.CODEC);
    public static final Supplier<TradeProvider> TRADE_TASK = register("trade_task", ()->TradeTask.CODEC);
    public static final Supplier<TradeProvider> ITEM_TRADE_LOOT_TABLE = register("ingredient_trade_loot_table", ()->ItemTradeLootTable.CODEC);
    public static final Supplier<TradeProvider> INGREDIENT_TRADE_ITEM_LIST = register("ingredient_trade_item_list", ()->ItemTradeItemList.CODEC);


    public static Supplier<TradeProvider> register(String name,
                                                    Supplier<MapCodec<? extends ITrade>> codec) {
        return TYPES.register(name, ()->new TradeProvider(codec));
    }
}
