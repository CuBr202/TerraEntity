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
import org.confluence.terraentity.registries.npc_trade_lock.ITradeLock;

import java.util.Optional;

/**
 * 战利品交易表
 * @param cost 花费的物品
 * @param lootTable 战利品掉落表
 */
public record ItemTradeLootTable(
        ItemStack cost,
        ResourceKey<LootTable> lootTable,
        Optional<ResourceLocation> sprite,
        ITradeLock lock
) implements IItemTrade, ITradeLootTable {

    public ItemTradeLootTable(ItemStack item,
                              ResourceLocation lootTable,
                              ResourceLocation sprite,
                              ITradeLock lock) {
        this(item, ResourceKey.create(Registries.LOOT_TABLE, lootTable), Optional.ofNullable(sprite), lock);
    }

    public static MapCodec<ItemTradeLootTable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ItemTradeLootTable::cost),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(i->i.lootTable().location()),
            ResourceLocation.CODEC.optionalFieldOf("sprite").forGetter(ItemTradeLootTable::sprite),
            ITradeLock.TYPED_CODEC.optionalFieldOf("lock").forGetter(i->Optional.ofNullable(i.lock))
    ).apply(instance, (item, lootTable, sprite,lock)->new ItemTradeLootTable(
            item,
            ResourceKey.create(Registries.LOOT_TABLE, lootTable),
            sprite,
            lock.orElse(null)
    )));

    public static ItemTradeLootTable of(ItemStack item, ResourceLocation lootTable, ResourceLocation sprite, ITradeLock lock) {
        return new ItemTradeLootTable(item, lootTable, sprite, lock);
    }

    public static ItemTradeLootTable of(ItemStack item, ResourceLocation lootTable, ResourceLocation sprite) {
        return new ItemTradeLootTable(item, lootTable, sprite, null);
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
