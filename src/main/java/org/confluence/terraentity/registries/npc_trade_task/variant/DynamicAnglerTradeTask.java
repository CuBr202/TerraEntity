package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.lib.util.LibUtils;
import org.confluence.terraentity.api.npc.trade.ITrade;
import org.confluence.terraentity.api.npc.trade.ITradeHolder;
import org.confluence.terraentity.api.npc.trade.ITradeLock;
import org.confluence.terraentity.api.npc.trade.ITradeTask;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItemList;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeLootTable;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProvider;
import org.confluence.terraentity.registries.npc_trade_task.TradeTaskProviderTypes;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>渔夫动态交易表
 * <P>相对于{@link DynamicPoolTradeTask},将战利品统一成{@link ItemTradeLootTable}和{@link ItemTradeItemList}
 * <P>resultPool存放每个等级对应的固定奖励List，cost来自于渔夫定时刷新，所以对于其他npc是没有用的
 */
public class DynamicAnglerTradeTask implements ITradeTask {
    public static final MapCodec<DynamicAnglerTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, ItemStack.CODEC.listOf()).xmap(
                    map -> map.entrySet().stream().map(entry -> Map.entry(Integer.parseInt(entry.getKey()), entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                    map -> map.entrySet().stream().map(entry -> Map.entry(entry.getKey().toString(), entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            ).fieldOf("result_pool").forGetter(task -> task.resultPool),
            ItemStack.CODEC.listOf().fieldOf("cost_pool").forGetter(task -> task.costPool),
            ITradeLock.TYPED_CODEC.listOf().fieldOf("cost_lock").forGetter(task -> task.costLock),
            Codec.STRING.optionalFieldOf("title").forGetter(i -> Optional.ofNullable(i.title)),
            ItemTradeLootTable.CODEC.fieldOf("default_trade").forGetter(task -> task.defaultTrade),
            ItemTradeItemList.CODEC.codec().optionalFieldOf("dynamic_trade").forGetter(task -> Optional.ofNullable(task.dynamicTrade)),
            Codec.INT.lenientOptionalFieldOf("current_selected", 0).forGetter(task -> task.currentSelected)
    ).apply(instance, (resultPool, costPool, costLock, title, defaultTrade, dynamicTrade, currentSelected) -> new DynamicAnglerTradeTask(
            resultPool,
            costPool,
            costLock,
            title.orElse(null),
            defaultTrade,
            dynamicTrade.orElse(null),
            currentSelected
    )));

    private final Map<Integer, List<ItemStack>> resultPool;
    private final List<ItemStack> costPool;
    private final List<ITradeLock> costLock;
    private final String title;

    private ItemTradeLootTable defaultTrade;
    private ItemTradeItemList dynamicTrade;
    private int currentSelected;

    /**
     * 用于数据生成
     * @param defaultTrade 默认奖励，渔夫使用{@link ItemTradeLootTable 战利品池交易表}
     * @param resultPool 等级对应的固定奖励池
     */
    public DynamicAnglerTradeTask(Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool, List<ITradeLock> costLock, @Nullable String title, ItemTradeLootTable defaultTrade, ItemTradeItemList dynamicTrade, int currentSelected) {
        if (costPool.size() != costLock.size()) {
            throw new IllegalArgumentException("costPool must match the size of costLock, but received costPool=" + costPool.size() + ", costLock=" + costLock.size());
        }
        this.resultPool = resultPool;
        this.costPool = costPool;
        this.costLock = costLock;
        this.title = title;
        this.defaultTrade = defaultTrade;
        this.dynamicTrade = dynamicTrade;
        this.currentSelected = currentSelected;
    }

    public ItemStack getCurrentCost() {
        return costPool.get(currentSelected).copy();
    }

    public ITradeLock getCurrentLock() {
        return costLock.get(currentSelected);
    }

    @Override
    public @Nullable ITrade getSelected(ITradeHolder npc, int index) {
        if (resultPool.containsKey(npc.getTradeParams().getLevel(index))) {
            return dynamicTrade == null ? defaultTrade : dynamicTrade;
        }
        return defaultTrade;
    }

    // 由于渔夫是一天一次，所以要setNext后不要立即同步数据
    @Override
    public void setNext(ITradeHolder npc, int index) {
        ItemStack cost = LibUtils.forMixin$ModifyExpression(costPool.get(this.currentSelected = npc.getRandom().nextInt(costPool.size())));
        List<ItemStack> result = resultPool.get(npc.getTradeParams().getLevel(index) + 1);

        if (result == null) {
            this.dynamicTrade = null;
            this.defaultTrade = new ItemTradeLootTable(
                    List.of(new AmountIngredient(Ingredient.of(cost), cost.getCount())),
                    defaultTrade.lootTable(),
                    defaultTrade.sprite(),
                    defaultTrade.translationKey(),
                    defaultTrade.properties()
            );
        } else {
            this.dynamicTrade = ItemTradeItemList.builder().addCost(cost).addResult(result).build();
        }
    }

    @Override
    public void afterTrade(ITradeHolder npc, int index) {
        // 更新参数
        npc.getTradeParams().setIsReady(index, false);
        npc.syncTradeTasksParams();
        // 添加到脏数据
        npc.getTradeManager().addToBeSync(index);
    }

    @Override
    public boolean canTrade(ITradeHolder npc, int index) {
        return npc.getTradeParams().isReady(index);
    }

    @Override
    public List<ITrade> getAllSupportedTrades() {
        return this.costPool.stream().map(s -> (ITrade) new ItemTradeLootTable(
                List.of(new AmountIngredient(Ingredient.of(s), s.getCount())),
                defaultTrade.lootTable(),
                defaultTrade.sprite(),
                defaultTrade.translationKey(),
                defaultTrade.properties()
        )).toList();
    }

    @Override
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.DYNAMIC_ANGLER_TRADE_TASK.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderCosts(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY) {
        int index = ITradeHolder.selectTradeIndex();
        int level = npc.getTradeParams().getLevel(index);
        boolean isReady = npc.getTradeParams().isReady(index);
        String info = "Day  " + level;
        guiGraphics.drawString(font, info, x, y, 0xFFFFFF);
        if (!isReady) {
            guiGraphics.drawString(font, "✓", x + font.width(info) + 10, y, 0x00FF00);
        }
    }

    /**
     * 用来切换标题
     */
    @Override
    public Component getTitle(ITradeHolder holder, Component original) {
        return Component.translatable(title() == null ? "title.terra_entity.npc_trade.task.daily" : title());
    }

    @Override
    public @Nullable String title() {
        return title;
    }

    public static Builder builder(ItemTradeLootTable defaultTrade, List<ItemStack> costPool) {
        return new Builder().setDefaultTrade(defaultTrade).setCostPool(costPool);
    }

    public static Builder builder(ItemTradeLootTable defaultTrade, Map<ItemStack, ITradeLock> costPool) {
        return new Builder().setDefaultTrade(defaultTrade).setCostPool(costPool);
    }

    public static class Builder {
        private final Map<Integer, List<ItemStack>> resultPool = new HashMap<>();
        private List<ItemStack> costPool;
        private List<ITradeLock> costLock;
        private String title;
        private ItemTradeLootTable defaultTrade;

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder addResult(int level, List<ItemStack> items) {
            this.resultPool.put(level, items);
            return this;
        }

        private Builder setCostPool(List<ItemStack> costPool) {
            this.costPool = costPool;
            this.costLock = NonNullList.withSize(costPool.size(), ITradeLock.alwaysTrue());
            return this;
        }

        private Builder setCostPool(Map<ItemStack, ITradeLock> costPool) {
            this.costPool = new ArrayList<>();
            this.costLock = new ArrayList<>();
            for (Map.Entry<ItemStack, ITradeLock> entry : costPool.entrySet()) {
                this.costPool.add(entry.getKey());
                this.costLock.add(entry.getValue());
            }
            return this;
        }

        private Builder setDefaultTrade(ItemTradeLootTable defaultTrade) {
            this.defaultTrade = defaultTrade;
            return this;
        }

        public DynamicAnglerTradeTask build() {
            return new DynamicAnglerTradeTask(resultPool, costPool, costLock, title, defaultTrade, null, 0);
        }
    }
}
