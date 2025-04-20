package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.IItemTrade;
import org.confluence.terraentity.registries.npc_trade.ITradeLootTable;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

import java.util.Optional;

/**
 * 战利品交易表
 * @param cost 花费的物品
 * @param lootTable 战利品掉落表
 */
public record ItemTradeLootTable(ItemStack cost, ResourceKey<LootTable> lootTable, Optional<ResourceLocation> sprite) implements IItemTrade, ITradeLootTable {

    public ItemTradeLootTable(ItemStack item, ResourceLocation lootTable, ResourceLocation sprite) {
        this(item, ResourceKey.create(Registries.LOOT_TABLE, lootTable), Optional.ofNullable(sprite));
    }

    public static MapCodec<ItemTradeLootTable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ItemTradeLootTable::cost),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(i->i.lootTable().location()),
            ResourceLocation.CODEC.optionalFieldOf("sprite").forGetter(ItemTradeLootTable::sprite)
    ).apply(instance, (item, lootTable, sprite)->new ItemTradeLootTable(item, ResourceKey.create(Registries.LOOT_TABLE, lootTable), sprite)));

    public static ItemTradeLootTable of(ItemStack item, ResourceLocation lootTable, ResourceLocation sprite) {
        return new ItemTradeLootTable(item, lootTable, sprite);
    }
    public static ItemTradeLootTable of(ItemStack item, ResourceLocation lootTable) {
        return of(item, lootTable, null);
    }


    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        IItemTrade.super.onTrade(player, npc, index);
        ITradeLootTable.super.onTrade(player, npc, index);
    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_LOOT_TABLE.get();
    }

}
