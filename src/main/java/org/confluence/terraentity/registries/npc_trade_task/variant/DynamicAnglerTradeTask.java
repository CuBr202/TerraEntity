package org.confluence.terraentity.registries.npc_trade_task.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.confluence.terraentity.entity.npc.trade.ITradeHolder;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeItemList;
import org.confluence.terraentity.registries.npc_trade.variant.ItemTradeLootTable;
import org.confluence.terraentity.registries.npc_trade_task.ITradeTask;
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

    private ItemTradeItemList dynamicTrade;

    private ItemTradeLootTable defaultTrade;
    private final Map<Integer, List<ItemStack>> resultPool;
    private final List<ItemStack> costPool;


    private final String title;

    public static final MapCodec<DynamicAnglerTradeTask> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemTradeLootTable.CODEC.fieldOf("default_trade").forGetter(DynamicAnglerTradeTask::getDefaultTrade),
            Codec.unboundedMap(Codec.STRING, ItemStack.CODEC.listOf()).fieldOf("result_pool").forGetter(
                    task -> task.resultPool.entrySet().stream()
                            .map(entry->new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
            ),
            ItemStack.CODEC.listOf().fieldOf("cost_pool").forGetter(DynamicAnglerTradeTask::getCostPool),
            ItemTradeItemList.CODEC.codec().optionalFieldOf("dynamic_trade").forGetter(DynamicAnglerTradeTask::getDynamicTrade),
            Codec.STRING.optionalFieldOf("title").forGetter(i->Optional.ofNullable(i.title))
    ).apply(instance, (defaultTrade, solid_rewards, costPool, dynamicTrade, title)->{
        return new DynamicAnglerTradeTask(
                defaultTrade,
                solid_rewards.entrySet()
                        .stream()
                        .map(entry->new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), entry.getValue()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)),
                costPool,
                dynamicTrade.orElse(null),
                title.orElse(null)
        );
    }));

    private List<ItemStack> getCostPool() {
        return costPool;
    }

    private Optional<ItemTradeItemList> getDynamicTrade() {
        return Optional.ofNullable(dynamicTrade);
    }


    private ItemTradeLootTable getDefaultTrade() {
        return defaultTrade;
    }

    /**
     * 用于数据生成
     * @param defaultTrade 默认奖励，渔夫使用{@link ItemTradeLootTable 战利品池交易表}
     * @param resultPool 等级对应的固定奖励池
     */
    public DynamicAnglerTradeTask(ItemTradeLootTable defaultTrade, Map<Integer, List<ItemStack>> resultPool, List<ItemStack> costPool, ItemTradeItemList dynamicTrade, @Nullable String title) {
        this.defaultTrade = defaultTrade;
        this.resultPool = resultPool;
        this.dynamicTrade = dynamicTrade;
        this.costPool = costPool;
        this.title = title;
    }

    @Override
    public @Nullable ITrade getSelected(ITradeHolder npc, int index) {
        int cur = npc.getTradeParams().getLevel(index);
        if(resultPool.containsKey(cur)){
            return dynamicTrade == null ? defaultTrade:dynamicTrade;
        }
        return defaultTrade;
    }

    // 由于渔夫是一天一次，所以要setNext后不要立即同步数据
    @Override
    public void setNext(ITradeHolder npc, int index) {
        int cur = npc.getTradeParams().getLevel(index)+1;
        int size = costPool.size();
        int randomIndex = npc.getRandom().nextInt(size);
        ItemStack cost = costPool.get(randomIndex);
        if(cost.isEmpty()){
            return;
        }
        if(resultPool.containsKey(cur)){
            dynamicTrade = ItemTradeItemList.of(cost, resultPool.get(cur));

        }else{
            dynamicTrade = null;
            defaultTrade = new ItemTradeLootTable(cost, defaultTrade.lootTable(), defaultTrade.sprite(), defaultTrade.translationKey(), defaultTrade.properties());
        }
    }


    @Override
    public void afterTrade(ITradeHolder npc, int index) {
//        ITradeTask.super.afterTrade(npc, index);

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
    public TradeTaskProvider getCodec() {
        return TradeTaskProviderTypes.DYNAMIC_ANGLER_TRADE_TASK.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderCosts(ITradeHolder npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx, int starty, int mouseX, int mouseY){
        int index = ITradeHolder.selectTradeIndex();
        int param = npc.getTradeParams().getLevel(index);
        boolean isReady = npc.getTradeParams().isReady(index);
        String info = "Day  " + param ;
        guiGraphics.drawString(font,  info, x, y, 0xFFFFFF);
        if(!isReady){
            guiGraphics.drawString(font, "√", x + font.width(info) + 10, y, 0x00FF00);

        }
    }

    /**
     * 用来切换标题
     */
    public Component getTitle(ITradeHolder holder, Component original){
        return Component.translatable(title() == null?"title.terra_entity.npc_trade.task.daily":title());
    }

    @Override
    public String title(){
        return title;
    }


    public static Builder builder(ItemTradeLootTable defaultTrade, List<ItemStack> costPool){
        return new Builder().setDefaultTrade(defaultTrade).setCostPool(costPool);
    }

    public static class Builder{
        private ItemTradeLootTable defaultTrade;
        private final Map<Integer, List<ItemStack>> resultPool = new HashMap<>();
        private List<ItemStack> costPool;
        private String title;

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDefaultTrade(ItemTradeLootTable defaultTrade) {
            this.defaultTrade = defaultTrade;
            return this;
        }

        public Builder addResult(int level, List<ItemStack> items) {
            this.resultPool.put(level, items);
            return this;
        }

        public Builder setCostPool(List<ItemStack> costPool) {
            this.costPool = costPool;
            return this;
        }

        public DynamicAnglerTradeTask build(){
            return new DynamicAnglerTradeTask(defaultTrade, resultPool, costPool, null, title);
        }
    }
}
