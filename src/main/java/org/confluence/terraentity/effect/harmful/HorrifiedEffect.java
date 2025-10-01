package org.confluence.terraentity.effect.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouse;
import org.confluence.terraentity.init.TEEffects;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;


public class HorrifiedEffect extends MobEffect {
    private WallOfFlesh wall;
    public HorrifiedEffect() {
        super(MobEffectCategory.HARMFUL, 0xAB1122);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity living, int amplifier) {
        if(wall !=null) {
            if(!living.getBoundingBox().intersects(wall.getOutsideCollisionBox())&&!living.getBoundingBox().intersects(wall.getInsideBox())||living.level().dimension()!=(wall.level().dimension())){
                living.kill();
            }else if(living.getBoundingBox().intersects(wall.getOutsideCollisionBox())&&!living.getBoundingBox().intersects(wall.getInsideBox())){
                Optional<WallOfFleshMouse> nearestMouth = wall.baseSegments.stream()
                        .filter(segment -> segment instanceof WallOfFleshMouse && segment.getY() > segment.level().getMinBuildHeight())
                        .map(segment -> (WallOfFleshMouse) segment)
                        .min(Comparator.comparingDouble(mouth ->
                                mouth.distanceToSqr(living)
                        ));

                nearestMouth.ifPresent(mouth -> {
                    RegistryObject<TheTongueEffect> horrifiedHolder = TEEffects.THE_TONGUE;
                    horrifiedHolder.get().setWallOfFleshMouth(mouth);
                    if(!living.hasEffect(TEEffects.THE_TONGUE.get()))living.addEffect(new MobEffectInstance(horrifiedHolder.get(), 60));
                });
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public void setWallOfFlesh(WallOfFlesh wall) {
        this.wall = wall;
    }
}
