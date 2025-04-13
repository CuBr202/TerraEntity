package org.confluence.terraentity.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.NPCAi;
import org.confluence.terraentity.registries.npc_trade.ITrade;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * NPC事件基类
 */
public abstract class NPCEvent  extends Event implements IModBusEvent {
    AbstractTerraNPC npc;

    public NPCEvent(AbstractTerraNPC npc) {
        this.npc = npc;
    }

    public AbstractTerraNPC getNPC() {
        return npc;
    }


    /**
     * 重定向交互npc事件，用于替换交互时打开的菜单
     */
    public static class InteractNPCEvent extends NPCEvent implements ICancellableEvent {
        private Player player;
        BiConsumer<AbstractTerraNPC, Player> reDirection;

        public InteractNPCEvent(AbstractTerraNPC npc, Player player) {
            super(npc);
            this.player = player;
        }

        public AbstractTerraNPC getNpc() {
            return npc;
        }

        public Player getPlayer() {
            return player;
        }

        /**
         * 设置重定向逻辑，当reDirection不为空时，使用这个逻辑
         */
        public void setRedirection(@Nonnull BiConsumer<AbstractTerraNPC, Player> reDirection) {
            this.reDirection = reDirection;
        }

        public void execute(BiConsumer<AbstractTerraNPC, Player> defaultAction) {
            if (reDirection != null) {
                reDirection.accept(npc, player);
            } else {
                defaultAction.accept(npc, player);
            }
        }

    }

    /**
     * 当初始化npc时触发，用于替换NPC交易列表
     */
    public static class InitNPCTradeEvent extends NPCEvent implements ICancellableEvent {
        private ResourceLocation origin;

        public InitNPCTradeEvent(AbstractTerraNPC npc, ResourceLocation origin) {
            super(npc);
            this.origin = origin;
        }

        /**
         * 设置重定向交易列表，当newResource不为空时，使用这个交易列表
         */
        public void setRedirection(@Nonnull ResourceLocation newResource) {
            this.origin = newResource;
        }

        public ResourceLocation getOrigin() {
            return origin;
        }
    }

    /**
     * 当交易时触发
     */
    public static class NPCTradeEvent extends NPCEvent implements IModBusEvent, ICancellableEvent {
        ITrade trade;
        Player player;
        boolean alwaysPass = false;
        BiConsumer<Player, ITrade> reDirection;

        public NPCTradeEvent(@Nullable AbstractTerraNPC npc, ITrade trade, Player player) {
            super(npc);
            this.trade = trade;
            this.player = player;
        }

        public ITrade getTrade() {
            return trade;
        }

        public Player getPlayer() {
            return player;
        }

        /**
         * 强行使交易通过
         */
        public void setAlwaysPass() {
            this.alwaysPass = true;
        }

        public boolean isAlwaysPass() {
            return alwaysPass;
        }

        /**
         * 当交易触发时，重新设置交易的逻辑，替换{@link org.confluence.terraentity.registries.npc_trade.ITrade#onTrade(net.minecraft.server.level.ServerPlayer)}
         */
        public void setRedirection(BiConsumer<Player, ITrade> reDirection) {
            this.reDirection = reDirection;
        }

        public BiConsumer<Player, ITrade> getRedirection() {
            return reDirection;
        }

    }

    /**
     * <p>当npc生成时，初始化brain时触发
     * <p>用于替换npc的brain
     * <p>因此所有的ai必须继承自{@link NPCAi}
     */
    public static class NPCBrainRegisterEvent extends NPCEvent implements IModBusEvent, ICancellableEvent {

        NPCAi replace;

        public NPCBrainRegisterEvent(AbstractTerraNPC npc) {
            super(npc);
        }

        /**
         * 设置替换brain，当replace不为空时，使用这个brain
         */
        public void setReplace(NPCAi replace) {
            this.replace = replace;
        }

        public NPCAi getReplace() {
            return replace;
        }

    }
}
