package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.Optional;

/**
 * 战利品交易表
 * @param cost 花费的物品
 * @param lootTable 战利品掉落表
 */
public record ItemTradeLootTable(
        ItemStack cost,
        ResourceKey<LootTable> lootTable,
        ResourceLocation sprite,
        String translationKey,
        TradeProperties properties
) implements IItemTrade, ITradeLootTable {

    public static MapCodec<ItemTradeLootTable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("cost").forGetter(ItemTradeLootTable::cost),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(i->i.lootTable().location()),
            ResourceLocation.CODEC.optionalFieldOf("sprite").forGetter(i->Optional.ofNullable(i.sprite())),
            Codec.STRING.optionalFieldOf("translation_key").forGetter(i->Optional.ofNullable(i.translationKey())),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i->Optional.ofNullable(i.properties))
    ).apply(instance, (item, lootTable, sprite, translationKey, properties1)->new ItemTradeLootTable(
            item,
            ResourceKey.create(Registries.LOOT_TABLE, lootTable),
            sprite.orElse(null),
            translationKey.orElse(null),
            properties1.orElse(null)
    )));

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private ItemStack cost;
        private ResourceLocation lootTable;
        private ResourceLocation sprite;
        private String translationKey;
        private TradeProperties properties;
        public Builder(){

        }
        public Builder setCost(ItemStack cost){
            this.cost = cost;
            return this;
        }
        public Builder setCost(ItemLike cost, int count){
            this.cost = new ItemStack(cost, count);
            return this;
        }
        public Builder setLootTable(ResourceLocation lootTable){
            this.lootTable = lootTable;
            return this;
        }
        public Builder setSprite(ResourceLocation sprite){
            this.sprite = sprite;
            return this;
        }
        public Builder setTranslationKey(String translationKey){
            this.translationKey = translationKey;
            return this;
        }
        public Builder setProperties(TradeProperties properties){
            this.properties = properties;
            return this;
        }
        public ItemTradeLootTable build(){
            return new ItemTradeLootTable(cost, ResourceKey.create(Registries.LOOT_TABLE, lootTable), sprite, translationKey, properties);
        }

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
