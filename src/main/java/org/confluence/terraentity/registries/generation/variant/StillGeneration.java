package org.confluence.terraentity.registries.generation.variant;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.generation.GenerationProvider;
import org.confluence.terraentity.registries.generation.GenerationProviderTypes;
import org.confluence.terraentity.registries.generation.IGeneration;
import org.confluence.terraentity.utils.TEUtils;
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
        Vec3 pos = owner.position().add(0,1,0);
        if(owner instanceof Player player){
            pos = pos.add(TEUtils.getPlayerHandPos(player));
        }
        projectile.setPos(pos);
        owner.level().addFreshEntity(projectile);
    }

    @Override
    public GenerationProvider getCodec() {
        return GenerationProviderTypes.FORWARD_GENERATION.get();
    }


}