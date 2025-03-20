package org.confluence.terraentity.entity.proj.generation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record StillGeneration(Vec3 offset) implements IGeneration {

    public static MapCodec<StillGeneration> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Vec3.CODEC.fieldOf("offset").forGetter(StillGeneration::offset)
    ).apply(instance, StillGeneration::new));

    public static StillGeneration of(Vec3 offset) {
        return new StillGeneration(offset);
    }

    @Override
    public void genProjectile(@NotNull LivingEntity owner, @Nullable ItemStack weapon, float velocity, @NotNull Supplier<? extends Projectile> proj) {
        Projectile projectile = proj.get();
        projectile.setOwner(owner);
        // todo 计算yaw
        projectile.setPos(owner.position().add(offset));
        owner.level().addFreshEntity(projectile);
    }

    @Override
    public GenerationProvider getCodec() {
        return GenerationProviderTypes.FORWARD_GENERATION.get();
    }


}