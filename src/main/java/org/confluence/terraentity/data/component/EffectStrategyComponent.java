package org.confluence.terraentity.data.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.confluence.terraentity.registries.hit_effect.EffectStrategy;
import org.confluence.terraentity.registries.hit_effect.IEffectStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 命中效果的数据生成器组件
 * @param effect 命中效果
 */
public record EffectStrategyComponent(IEffectStrategy effect) implements DataComponentType<EffectStrategyComponent> {
    public static final Codec<EffectStrategyComponent> CODEC = IEffectStrategy.TYPED_CODEC.xmap(EffectStrategyComponent::new, EffectStrategyComponent::effect);
    public static final StreamCodec<ByteBuf, EffectStrategyComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public EffectStrategyComponent(EffectStrategy effect) {
        this(effect.getProvider());
    }

    public static EffectStrategyComponent of(IEffectStrategy effect) {
        return new EffectStrategyComponent(effect);
    }

    public static EffectStrategyComponent of(EffectStrategy effect) {
        return new EffectStrategyComponent(effect.getProvider());
    }


    @Override
    public @Nullable Codec<EffectStrategyComponent> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, EffectStrategyComponent> streamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EffectStrategyComponent(IEffectStrategy effect1) && effect1 == effect;
    }
}
