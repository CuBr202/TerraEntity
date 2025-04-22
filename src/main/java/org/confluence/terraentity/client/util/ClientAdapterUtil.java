package org.confluence.terraentity.client.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ClientAdapterUtil {
    public static void blitSprite(GuiGraphics graphics, ResourceLocation sprite, int x, int y, int width, int height){
        graphics.blit(sprite.withPrefix("textures/gui/sprites/").withSuffix(".png"), x,y,0,0,width,height,width,height);
    }
}
