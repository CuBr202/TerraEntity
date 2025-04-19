package org.confluence.terraentity.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.ai.keyframe.animation.KeyframeAnimation;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.NPCDialogs;
import org.confluence.terraentity.menu.TETradesMenu;
import org.confluence.terraentity.mixed.IPlayer;
import org.confluence.terraentity.registries.npc_trade.ITrade;

import java.util.List;

/**
 * <p>由于交易的获得的内容是单个，统一使用trade的抽象菜单类
 * <p>渲染cost的逻辑在{@link org.confluence.terraentity.registries.npc_trade.ITrade#renderCosts(GuiGraphics, Font, int, int, int, int, int, int)}
 * <p>使用时必须继承此类，否则会出现类型推断不匹配</p>
 */
public abstract class TETradeScreen< M extends TETradesMenu> extends AbstractContainerScreen<M> {
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/villager/scroller_disabled");
    public static final ResourceLocation MENU_LOCATION = TerraEntity.space("textures/gui/container/npc_shop.png");
    private static final int NUMBER_OF_LINES = 7;
    private static final Component TRADES_LABEL = Component.translatable("title.terra_entity.npc_trade");

    private int shopItem = -1;
    private int hoveredItem = -1;
    private int row;
    private final int col = 5;
    private int offsetX;
    private int offsetY;
    private int intervalX = 18;
    private int intervalY = 18;

    int tickCount;

    int scrollOff;

    KeyframeAnimation interpolator;
    boolean triggerOnce = true;

    public TETradeScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 290;
        this.inventoryLabelX = 107;

    }

    @Override
    protected void init() {
        super.init();
        if (menu.NPCTrades == null) {
            menu.NPCTrades = ((IPlayer) Minecraft.getInstance().player).terra_entity$getDaveTrades();
            if (menu.NPCTrades == null){
                return;
            }
        }
        this.row = menu.NPCTrades.trades().size() / 3;
        if (menu.NPCTrades.trades().size() % 3 != 0)
            this.row++;

        offsetX = (this.width - this.imageWidth) / 2 + 5;
        offsetY = (this.height - this.imageHeight) / 2 + 16;

        interpolator = KeyframeAnimation.Builder()
                .addKeyframe(5, 0)
                .addKeyframe(20, 40)
                .addKeyframe(30, 55)
                .addKeyframe(40, 60)
                .build();

        if(triggerOnce) {
            // 如果没有对话，则不显示对话框
//            if(((IPlayer) Minecraft.getInstance().player).terra_entity$getInteractingEntity() instanceof AbstractTerraNPC npc){
//                if(NPCDialogs.getDialog_map().get(BuiltInRegistries.ENTITY_TYPE.getKey(npc.getType())) != null) {
                    Minecraft.getInstance().setScreen(new DialogScreen(Component.literal("123"), this));
//                }
//            }
            triggerOnce = false;
        }

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        guiGraphics.setColor(1, 1, 1, (float)(v / 60f));
        int fy = 6 - (int)((60 - v) / 5);

        guiGraphics.drawString(this.font, ((MutableComponent)this.title).withStyle(Style.EMPTY.withBold(true)), 49 + this.imageWidth / 2 - this.font.width(this.title) / 2, fy, 0xFF5656, false);
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.drawString(this.font, this.playerInventoryTitle,90 + this.imageWidth / 2, this.inventoryLabelY, 4210752, false);
        Entity interactEntity = ((IPlayer)minecraft.player).terra_entity$getInteractingEntity();
        Component title = interactEntity == null || interactEntity.getDisplayName() == null? TRADES_LABEL : interactEntity.getDisplayName();

        int l = this.font.width(title);

        guiGraphics.drawString(this.font, title, 5 - l / 2 + 48, 6, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(MENU_LOCATION, i, j, 0, 0.0F, 0.0F, this.imageWidth-15, this.imageHeight, 512, 256);
    }

    private void renderScroller(GuiGraphics guiGraphics, int posX, int posY) {

        int i = menu.NPCTrades.trades().size() - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }
            guiGraphics.blitSprite(SCROLLER_SPRITE, posX + 95, posY + 17 + i1, 0, 6, 27);
        } else {
            guiGraphics.blitSprite(SCROLLER_DISABLED_SPRITE, posX + 95, posY + 17, 0, 6, 27);
        }
    }

    @Override
    protected void containerTick() {

        this.tickCount++;
    }

    double v;
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(interpolator == null) return;

        if (menu.NPCTrades == null) {
            menu.NPCTrades = ((IPlayer) Minecraft.getInstance().player).terra_entity$getDaveTrades();
            if (menu.NPCTrades == null){
                return;
            }
        }
        this.row = menu.NPCTrades.trades().size() / 3;
        if (menu.NPCTrades.trades().size() % 3 != 0)
            this.row++;


        v = interpolator.cal(this.tickCount + partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);


        int ii = (this.width - this.imageWidth) / 2;
        int jj = (this.height - this.imageHeight) / 2;
        this.renderScroller(guiGraphics, ii, jj);

        // 左侧物品
        if(this.hoveredItem >= 0 && this.hoveredItem < menu.NPCTrades.trades().size()){
            int x = offsetX + hoveredItem % col * intervalX;
            int y = offsetY + (hoveredItem / col - scrollOff) * intervalY;
            renderSlotHighlight(guiGraphics,x ,y, 20);
        }
        // 左侧物品
        if(this.shopItem >= 0 && this.shopItem < menu.NPCTrades.trades().size()){
            int x = offsetX + shopItem % col * intervalX;
            int y = offsetY + (shopItem / col- scrollOff) * intervalY;
            renderSlotHighlight(guiGraphics,x ,y , 20);
        }
        List<ITrade> trades = menu.NPCTrades.trades();
        AbstractTerraNPC npc = (AbstractTerraNPC) ((IPlayer) Minecraft.getInstance().player).terra_entity$getInteractingEntity();

        int x = offsetX;
        int y = offsetY;
        int xcache = x;
        int ycache = y;
        int cacheIndex = -1;
        for (int l = 0; l < Math.min(row, NUMBER_OF_LINES); l++) {
            for(int k = 0; k < col; k++){
                int index = k+(l+ scrollOff) * col;
                if(index >= trades.size()) break;
                var trade = trades.get(index);

                // 渲染获得的物品
                renderResult(npc, guiGraphics, font, x, y, ii, jj, mouseX, mouseY, trade);
                if(mouseX > x && mouseX < x+16 && mouseY > y && mouseY < y+16){
                    xcache = x;
                    ycache = y;
                    cacheIndex = index;
                }

                x+=intervalX;
            }
            x = offsetX;
            y += intervalY;
        }

        // 如果选择了交易项
        // 渲染上面的材料物品
        if(shopItem < 0 ||shopItem >= trades.size())
            return;
        var trade = trades.get(this.shopItem);
        x = ii + 116;
        y = jj + 19;
        renderCosts(npc, guiGraphics, font,  x, y, ii, jj, mouseX, mouseY, trade);


        x = ii + 203;
        y = jj + 36;
        // 能否购买
        boolean canBuy = trade.canTrade(Minecraft.getInstance().player, npc);
        renderResultSlot(npc,guiGraphics, font, x, y, ii, jj, mouseX, mouseY, trade, canBuy);

        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // 重新渲染悬浮时物品信息
        if(cacheIndex != -1){
            renderResultHover(npc,guiGraphics, font, xcache, ycache, ii, jj, mouseX, mouseY, trades.get(cacheIndex));
        }

    }

    protected void renderCosts(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx,int starty,int mouseX, int mouseY, ITrade trade){
        trade.renderCosts(npc,guiGraphics, font, x, y, startx, starty, mouseX, mouseY);
    }

    protected void renderResult(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx,int starty,int mouseX, int mouseY, ITrade trade){
        trade.renderResult(npc,guiGraphics, font, x, y, startx, starty, mouseX, mouseY);
    }
    protected void renderResultHover(AbstractTerraNPC npc, GuiGraphics guiGraphics, Font font, int x, int y, int startx,int starty,int mouseX, int mouseY, ITrade trade){
        trade.renderResultHover(npc,guiGraphics, font, x, y, startx, starty, mouseX, mouseY);
    }

    protected void renderResultSlot(AbstractTerraNPC npc, GuiGraphics guiGraphics,Font font, int x, int y, int startx,int starty,int mouseX, int mouseY, ITrade trade, boolean canBuy){
        trade.renderResultSlot(npc,guiGraphics,font, x, y, startx, starty, mouseX, mouseY, canBuy, menu.slots.get(0));
    }


    private boolean canScroll() {
        return row > NUMBER_OF_LINES;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int i = row;
        if (this.canScroll()) {
            int j = i - NUMBER_OF_LINES;
            this.scrollOff = Mth.clamp((int)((double)this.scrollOff - scrollY), 0, j);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        if (
                mouseX > (double)(i + 238) && mouseX <= (double)(i + 238 + 16) &&
                mouseY > (double)(j + 36)&& mouseY <= (double)(j + 36 + 16)
        ) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        this.shopItem = hoveredItem;
        menu.selectedMerchantIndex = shopItem;
        if(menu.selectedMerchantIndex <0) menu.slots.get(0).set(ItemStack.EMPTY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {

        int x = (int) (mouseX - offsetX);
        int y = (int) (mouseY - offsetY);
        int i = x / intervalX;
        int j = y / intervalY;
        if (i >= 0 && i < col && j >= 0 && j < row
                && x % intervalX < 16 && y % intervalY < 16
                && x >= 0 && y >= 0
        ) {
            int index = i + (j + scrollOff) * col;
            if (index < menu.NPCTrades.trades().size()) {
                this.hoveredItem = index;

            }
        }else {
            this.hoveredItem = -1;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class TradeOfferButton extends Button {
        final int index;
        public TradeOfferButton(int x, int y, int index, OnPress onPress) {
            super(x, y, 88, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.index = index;
            this.visible = false;
        }
        public int getIndex() {
            return this.index;
        }
    }
}
