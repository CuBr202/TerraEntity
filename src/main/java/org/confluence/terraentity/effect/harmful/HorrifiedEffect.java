package org.confluence.terraentity.effect.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouse;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshPart;
import org.confluence.terraentity.init.TEEffects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class HorrifiedEffect extends MobEffect {
    private WallOfFlesh wall;
    public HorrifiedEffect() {
        super(MobEffectCategory.HARMFUL, 0xAB1122);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if(wall !=null) {
            if(!living.getBoundingBox().intersects(wall.getOutsideCollisionBox())&&!living.getBoundingBox().intersects(wall.getInsideBox())||living.level().dimension()!=(wall.level().dimension())){
                living.kill();
            }else if(living.getBoundingBox().intersects(wall.getOutsideCollisionBox())&&!living.getBoundingBox().intersects(wall.getInsideBox())){
                List<WallOfFleshMouse> nearestMouths = new ArrayList<>();
                for (int i = 0; i < wall.subEntities.size(); i++) {
                    WallOfFleshPart segment = wall.subEntities.get(i);
                    if (segment instanceof WallOfFleshMouse mouse && segment.getY() > segment.level().getMinBuildHeight()) {
                        nearestMouths.add(mouse);
                    }
                }
                // 按距离排序获取最近的20个
                nearestMouths.sort(Comparator.comparingDouble(mouth -> mouth.distanceToSqr(living)));
                List<WallOfFleshMouse> nearest20 = nearestMouths.subList(0, Math.min(20, nearestMouths.size()));

                WallOfFleshMouse targetMouth = null;
                for (WallOfFleshMouse mouse : nearest20) {
                    if (!mouse.isInWall()) {
                        targetMouth = mouse;
                        break;
                    }
                }

                // 如果没有找到合适的嘴，返回最近的嘴
                if (targetMouth == null && !nearest20.isEmpty()) {
                    targetMouth = nearest20.get(0);
                }

                if (targetMouth != null) {
                    DeferredHolder<MobEffect, TheTongueEffect> horrifiedHolder = TEEffects.THE_TONGUE;
                    horrifiedHolder.get().setWallOfFleshMouth(targetMouth);
                    if(!living.hasEffect(TEEffects.THE_TONGUE))
                        living.addEffect(new MobEffectInstance(horrifiedHolder, 60));
                }
            }
        }else living.getActiveEffectsMap().remove(TEEffects.HORRIFIED).getEffect();
        return true;
    }


    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public void setWallOfFlesh(WallOfFlesh wall) {
        this.wall = wall;
    }
}
