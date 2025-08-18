package org.confluence.terraentity.effect.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouth;
import org.confluence.terraentity.init.TEEffects;

import java.util.Comparator;
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
                Optional<WallOfFleshMouth> nearestMouth = wall.baseSegments.stream()
                        .filter(segment -> segment instanceof WallOfFleshMouth  && segment.getY() > segment.level().getMinBuildHeight())
                        .map(segment -> (WallOfFleshMouth) segment)
                        .min(Comparator.comparingDouble(mouth ->
                                mouth.distanceToSqr(living)
                        ));

                nearestMouth.ifPresent(mouth -> {
                    DeferredHolder<MobEffect, TheTongueEffect> horrifiedHolder = TEEffects.THE_TONGUE;
                    horrifiedHolder.get().setWallOfFleshMouth(mouth);
                    if(!living.hasEffect(TEEffects.THE_TONGUE))
                        living.addEffect(new MobEffectInstance(horrifiedHolder, 60));
                });
            }
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public void setWallOfFlesh(WallOfFlesh wall) {
        this.wall = wall;
    }
}
