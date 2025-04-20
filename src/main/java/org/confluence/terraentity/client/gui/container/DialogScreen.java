package org.confluence.terraentity.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.entity.npc.misc.NPCDialogs;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.mixed.IPlayer;

public class DialogScreen extends Screen {
    Button button;
    Screen parent;
    AbstractTerraNPC entity;
    Component dialogText;
    protected DialogScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        if(((IPlayer) Minecraft.getInstance().player).terra_entity$getTradeHolder() instanceof AbstractTerraNPC npc){
            entity = npc;
            String dialog = NPCDialogs.getRandomDialog(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
            if(dialog!= null) {
                dialogText = Component.translatable(dialog);
            }
        }
        button = Button.builder(Component.literal("Trade"), p->{
            minecraft.setScreen(parent);
        }).pos(width/2 - 80, height / 2 + 25).build();

        addRenderableWidget(button);
    }

    @Override
    public void onClose() {
//        super.onClose();
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(dialogText != null){
            guiGraphics.drawString(font, dialogText, 20, height / 2 -10, 0xFFFFFF);
        }

        // todo draw
        if(entity.getMood() == null){
            return;
        }
        var list = entity.getMood().getMoodInfoList();
        for(int i = 0; i < list.size(); i++){
            ResourceLocation location = list.get(i);

            MoodInfo moodInfo = NPCMoods.getMoodInfo(location);

            if(moodInfo == null) continue;
            guiGraphics.drawString(font, Component.translatable(moodInfo.info()), 20, height / 2 - 100 + i * 10, 0xFFFFFF);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 69) { // E
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
