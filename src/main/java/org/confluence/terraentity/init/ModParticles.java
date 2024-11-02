package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, TerraEntity.MODID);

    public static final Supplier<SimpleParticleType> ITEM_GEL = PARTICLES.register("item_gel", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ITEM_PINK_GEL = PARTICLES.register("item_pink_gel", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RUBY_BULLET = PARTICLES.register("ruby_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> AMBER_BULLET = PARTICLES.register("amber_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> TOPAZ_BULLET = PARTICLES.register("topaz_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> EMERALD_BULLET = PARTICLES.register("emerald_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SAPPHIRE_BULLET = PARTICLES.register("sapphire_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> DIAMOND_BULLET = PARTICLES.register("diamond_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> AMETHYST_BULLET = PARTICLES.register("amethyst_bullet", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FLAMEFLOWER_BLOOM = PARTICLES.register("flameflower_bloom", () -> new SimpleParticleType(false));
//    public static final Supplier<ParticleType<CurrentDustOptions>> CURRENT_DUST = register("current_dust", false, CurrentDustOptions.DESERIALIZER, (p_123819_) -> CurrentDustOptions.CODEC);
//    public static final Supplier<ParticleType<BloodParticleOptions>> BLOOD = register("blood", false, BloodParticleOptions.DESERIALIZER, type-> BloodParticleOptions.CODEC);
//    public static final Supplier<ParticleType<BodyPartsParticleOptions>> BODY_PART = register("body_part", false, BodyPartsParticleOptions.DESERIALIZER, type-> BodyPartsParticleOptions.CODEC);
//    public static final Supplier<ParticleType<LightsBaneParticleOptions>> LIGHTS_BANE = register("lights_bane", true, LightsBaneParticleOptions.DESERIALIZER, type-> LightsBaneParticleOptions.CODEC);
//    public static final Supplier<SimpleParticleType> LIGHTS_BANE_DUST = PARTICLES.register("lights_bane_dust", () -> new SimpleParticleType(true));
//    public static final Supplier<SimpleParticleType> LIGHTS_BANE_FADE = PARTICLES.register("lights_bane_fade", () -> new SimpleParticleType(true));
//    public static final Supplier<ParticleType<DamageIndicatorOptions>> DAMAGE_INDICATOR = register("damage_indicator", false, DamageIndicatorOptions.DESERIALIZER, type-> DamageIndicatorOptions.CODEC);

//    @SuppressWarnings("all")
//    private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
//        return PARTICLES.register(pKey, () -> new ParticleType<T>(pOverrideLimiter, pDeserializer) {
//            public @NotNull Codec<T> codec() {
//                return pCodecFactory.apply(this);
//            }
//        });
//    }
}
