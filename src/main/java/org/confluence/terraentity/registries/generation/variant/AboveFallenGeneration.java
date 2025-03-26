package org.confluence.terraentity.registries.generation.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.generation.IGeneration;
import org.confluence.terraentity.utils.TEUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * 星怒弹幕发射方式
 * @param maxAngle 索敌最大角度
 * @param range 索敌范围
 * @param predict 预判量
 * @param inAccuracy 不精准度
 * @param offsetV 发射时的高度偏移
 * @param offsetH 发射时的xy偏移
 */
public record AboveFallenGeneration(float maxAngle, float range, float predict, float inAccuracy, float offsetV, float offsetH) implements IGeneration {

    public static MapCodec<AboveFallenGeneration> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.FLOAT.fieldOf("max_angle").forGetter(AboveFallenGeneration::maxAngle),
            Codec.FLOAT.fieldOf("range").forGetter(AboveFallenGeneration::range),
            Codec.FLOAT.fieldOf("predict").forGetter(AboveFallenGeneration::predict),
            Codec.FLOAT.fieldOf("in_accuracy").forGetter(AboveFallenGeneration::inAccuracy),
            Codec.FLOAT.fieldOf("offset_v").forGetter(AboveFallenGeneration::offsetV),
            Codec.FLOAT.fieldOf("offset_h").forGetter(AboveFallenGeneration::offsetH)
    ).apply(instance, AboveFallenGeneration::new));


    @Override
    public void genProjectile(@NotNull LivingEntity owner, ItemStack weapon, float velocity, @NotNull Supplier<? extends Projectile> proj) {
        var projectile = proj.get();
        Vec3 eye = owner.getEyePosition();
        LivingEntity target = TEUtils.getAABBAngleTarget(eye, eye.add(owner.getForward().normalize().scale(range)), owner.level(), owner, range, maxAngle, e->TEUtils.projectileCanHitEntityTest.test(projectile,e));
        Vec3 waveTarget;
        float angle;
        float actualInaccuracy;
        if(target!=null){
            //周围有目标 预判
            waveTarget = target.getEyePosition().add(target.getDeltaMovement().scale(predict));
            //根据夹角减少不精准度
            angle = (float) TEUtils.angleBetween(target.getEyePosition().subtract(owner.getEyePosition()),owner.getForward());
            actualInaccuracy = inAccuracy * Mth.lerp(angle/maxAngle,0,inAccuracy * 500) * 5;
        }else{
            //周围无目标 获取视线指向点
            Vec3 ori = owner.getEyePosition().add(0,1,0);
            Vec3 end = ori.add(owner.getForward().normalize().scale(range));
            BlockHitResult blockHitResult = owner.level().clip(new ClipContext(ori,end, ClipContext.Block.OUTLINE,ClipContext.Fluid.NONE, owner));
            waveTarget = blockHitResult.getLocation();
            //取中值
            actualInaccuracy = inAccuracy / 2;
        }

        projectile.setOwner(owner);
        projectile.setPos(waveTarget.add(Math.random() * offsetH - offsetH, offsetV ,Math.random() * offsetH - offsetH));
        projectile.shoot(waveTarget.x - projectile.getX(),waveTarget.y- projectile.getY(),waveTarget.z - projectile.getZ(), velocity, actualInaccuracy);
        owner.level().addFreshEntity(projectile);
    }


    @Override
    public GenerationProvider getCodec() {
        return GenerationProviderTypes.ABOVE_FALLEN.get();
    }

}
