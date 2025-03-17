package org.confluence.terraentity.entity.proj.generation;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.registries.GenerationProviders;
import org.confluence.terraentity.registries.TrackTypeProviders;

import java.util.function.Supplier;

/**
 * <h1>发射方式</h1>
 */
public interface IGeneration {

    /**
     * 生成弹道实体
     */
    void genProjectile(LivingEntity owner, ItemStack weapon, float velocity, Supplier<? extends Projectile> proj);
    /**
     * 获取编解码器
     * @return 编解码器
     */
    GenerationProvider getCodec();

    Codec<IGeneration> TYPED_CODEC = GenerationProviders.GENERATION_PROVIDER_REGISTRY
            .byNameCodec()
            .dispatch(IGeneration::getCodec, GenerationProvider::codec);
}
