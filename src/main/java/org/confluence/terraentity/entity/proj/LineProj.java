package org.confluence.terraentity.entity.proj;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class LineProj extends BaseProj<LineProj> {
    private int existTick;

    @Override
    public int getLifetime() {
        return existTick;
    }

    public LineProj(EntityType<? extends LineProj> pEntityType, Level pLevel) {
        this(pEntityType,pLevel,(MobEffectInstance)null);
    }
    public LineProj(EntityType<? extends LineProj> pEntityType, Level pLevel,MobEffectInstance effect) {
        super(pEntityType,pLevel,effect);
        this.existTick = 20 * 5;
    }
    public LineProj(EntityType<? extends LineProj> pEntityType, Level pLevel,List<MobEffectInstance> effects) {
        super(pEntityType,pLevel,effects);
        this.existTick = 20 * 5;
    }

    public LineProj setExistTick(int existTick) {
        this.existTick = existTick;
        return this;
    }


    @Override
    public void tick(){
        super.tick();
    }
}
