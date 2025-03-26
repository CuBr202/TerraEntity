package org.confluence.terraentity.registries.generation;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.confluence.terraentity.registries.TERegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * <h1>发射方式</h1>
 */
public interface IGeneration {

    /**
     * 生成弹道实体
     */
    void genProjectile(@NotNull LivingEntity owner, @Nullable ItemStack weapon, float velocity, @NotNull Supplier<? extends Projectile> proj);

    /**
     * 获取编解码器
     * @return 编解码器
     */
    GenerationProvider getCodec();

    MapCodec<IGeneration> TYPED_CODEC = TERegistries.GenerationProviders.REGISTRY.get()
            .getCodec()
            .dispatchMap(IGeneration::getCodec, c->c.codec().codec());
}
