package org.confluence.terraentity.registries.npc_trade.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 物品交换其他的东西，统一使用这个类
 * @param costs 花费的物品原料
 * @param result 获得的物品列表
 * @param properties 交易属性
 */
public record IngredientsTradeItemList(List<AmountIngredient> costs, List<ItemStack> result, TradeProperties properties) implements IIngredientTrade, ITradeItemList {

    public static MapCodec<IngredientsTradeItemList> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AmountIngredient.CODEC.codec().listOf().fieldOf("costs").forGetter(IngredientsTradeItemList::costs),
            ItemStack.CODEC.listOf().fieldOf("result").forGetter(IngredientsTradeItemList::result),
            TradeProperties.CODEC.optionalFieldOf("properties").forGetter(i-> Optional.ofNullable(i.properties))
    ).apply(instance, (costs, result, properties)->new IngredientsTradeItemList(
            costs,
            result,
            properties.orElse(null)
    )));


    public static class Builder{
        private final List<AmountIngredient> costs = new ArrayList<>();
        private final List<ItemStack> result = new ArrayList<>();
        private TradeProperties properties;

        public Builder addCost(AmountIngredient cost) {
            this.costs.add(cost);
            return this;
        }

        public Builder addCosts(ItemLike item, int count) {
            return addCost(new AmountIngredient(Ingredient.of(item), count));
        }

        public Builder addCosts(TagKey<Item> tag, int count) {
            return addCost(new AmountIngredient(Ingredient.of(tag), count));
        }

        public Builder addResult(ItemLike item, int count) {
            return addResult(new ItemStack(item, count));
        }

        public Builder addResult(ItemStack result) {
            this.result.add(result);
            return this;
        }

        public Builder setProperties(TradeProperties properties) {
            this.properties = properties;
            return this;
        }

        public IngredientsTradeItemList build(){
            return new IngredientsTradeItemList(costs, result, properties);
        }
    }

    @Override
    public void onTrade(ServerPlayer player, ITradeHolder npc, int index) {
        IIngredientTrade.super.onTrade(player, npc, index);
        ITradeItemList.super.onTrade(player, npc, index);

    }

    @Override
    public TradeProvider getCodec() {
        return TradeProviderTypes.INGREDIENT_TRADE_ITEM_LIST.get();
    }


}
