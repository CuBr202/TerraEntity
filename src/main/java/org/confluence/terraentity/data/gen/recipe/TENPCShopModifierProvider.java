package org.confluence.terraentity.data.gen.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.gen.AbstractExistCodecProvider;
import org.confluence.terraentity.entity.npc.trade.TradeModifiers;
import org.confluence.terraentity.init.entity.TENpcEntities;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItemList;
import org.confluence.terraentity.registries.npc_trade_modify.ITradeModifier;
import org.confluence.terraentity.registries.npc_trade_modify.variant.TradeItemModifier;
import org.confluence.terraentity.registries.npc_trade_modify.variant.TradeListModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用来测试，发布时应该删掉
 */
public class TENPCShopModifierProvider extends AbstractExistCodecProvider<List<ITradeModifier>> {

    public TENPCShopModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void run(HolderLookup.Provider provider) {

        this.gen(TerraEntity.space(TradeModifiers.KEY + "/modify_merchant1"), ImmutableList.<ITradeModifier>builder()
                .add(new TradeItemModifier(1, 0, TENpcEntities.MERCHANT.getId(),  ITradeModifier.OperatorType.ADD, ItemTradeItemList.builder()
                        .addCost(Items.DIAMOND, 10).addResult(Items.EMERALD, 1)
                        .build()))
                .build());

        this.gen(TerraEntity.space(TradeModifiers.KEY + "/modify_merchant2"), ImmutableList.<ITradeModifier>builder()
                .add(new TradeItemModifier(1, 3, TENpcEntities.MERCHANT.getId(),  ITradeModifier.OperatorType.ADD, ItemTradeItemList.builder()
                        .addCost(Items.DIAMOND, 15).addResult(Items.EMERALD, 2)
                        .build()))
                .build());

        this.gen(TerraEntity.space(TradeModifiers.KEY + "/del_guide"), ImmutableList.<ITradeModifier>builder()
                .add(new TradeListModifier(1, TENpcEntities.GUIDE.getId(),  ITradeModifier.OperatorType.DEL,null))
                .build()
        );

    }


    @Override
    protected Codec<List<ITradeModifier>> getCodec() {
        return TradeModifiers.CODEC;
    }

    @Override
    public String getName() {
        return "Trade Modifier";
    }
}
