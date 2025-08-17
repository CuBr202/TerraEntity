package org.confluence.terraentity.client.buffer;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.confluence.terraentity.item.BossSummonsItem;

import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;


/**
 * 用于显示Debug实体
 */
public enum DebugEntityHelper{
    INSTANCE;

    Color color = Color.white;
    Entity e;

    DebugEntityHelper() {
    }

    private boolean shouldRender() {
        ItemStack it = Minecraft.getInstance().player.getMainHandItem();
        if(it.getItem() instanceof BossSummonsItem<?> summonsItem) {
            if(summonsItem.hasSpecificSummonPos()){
                return false;
            }
            color = Color.WHITE;
            if(e == null || summonsItem.getEntityType() != e.getType()){
                e = summonsItem.getEntityType().create(Minecraft.getInstance().level);
            }
            Vec3 eyePos = summonsItem.getSummonPos(Minecraft.getInstance().player, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
            e.setPos(eyePos);
            return true;
        }
        return false;
    }

    public void render(RenderLevelStageEvent event){
        if(!shouldRender()){
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        Vec3 playerPos = event.getCamera().getPosition();
        var render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(e);
        if(render instanceof GeoEntityRenderer renderer){
            poseStack.pushPose();
            poseStack.translate(e.getX() - playerPos.x(), e.getY() - playerPos.y() , e.getZ() - playerPos.z());

            renderer.render(e, 0, 0, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), 15<<20| 15 << 4);
            poseStack.popPose();
        }

    }
}
