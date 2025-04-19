package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.registries.npc_trade.IItemTrade;
import org.confluence.terraentity.registries.npc_trade.ITradeLootTable;
import org.confluence.terraentity.registries.npc_trade.TradeProvider;
import org.confluence.terraentity.registries.npc_trade.TradeProviderTypes;

public record ItemTradeLootTable(ItemStack cost, ResourceKey<LootTable> lootTable) implements IItemTrade, ITradeLootTable {

    public ItemTradeLootTable(ItemStack item, ResourceLocation lootTable) {
        this(item, ResourceKey.create(Registries.LOOT_TABLE, lootTable));
    }

    public static MapCodec<ItemTradeLootTable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ItemTradeLootTable::cost),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(i->i.lootTable().location())
    ).apply(instance, ItemTradeLootTable::new));

    public static ItemTradeLootTable of(ItemStack item, ResourceLocation lootTable) {
        return new ItemTradeLootTable(item, lootTable);
    }



    @Override
    public void onTrade(ServerPlayer player, AbstractTerraNPC npc) {
        IItemTrade.super.onTrade(player, npc);
        ITradeLootTable.super.onTrade(player, npc);
    }


    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.ITEM_TRADE_LOOT_TABLE.get();
    }

}
