package org.confluence.terraentity.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;

import java.util.function.Supplier;

public final class TEParticles {
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
//    public static final Supplier<ParticleType<CurrentDustOptions>> CURRENT_DUST = registerEgg("current_dust", false, CurrentDustOptions.DESERIALIZER, (p_123819_) -> CurrentDustOptions.CODEC);
//    public static final Supplier<ParticleType<BloodParticleOptions>> BLOOD = registerEgg("blood", false, BloodParticleOptions.DESERIALIZER, type-> BloodParticleOptions.CODEC);
//    public static final Supplier<ParticleType<BodyPartsParticleOptions>> BODY_PART = registerEgg("body_part", false, BodyPartsParticleOptions.DESERIALIZER, type-> BodyPartsParticleOptions.CODEC);
//    public static final Supplier<ParticleType<LightsBaneParticleOptions>> LIGHTS_BANE = registerEgg("lights_bane", true, LightsBaneParticleOptions.DESERIALIZER, type-> LightsBaneParticleOptions.CODEC);
//    public static final Supplier<SimpleParticleType> LIGHTS_BANE_DUST = PARTICLES.registerEgg("lights_bane_dust", () -> new SimpleParticleType(true));
//    public static final Supplier<SimpleParticleType> LIGHTS_BANE_FADE = PARTICLES.registerEgg("lights_bane_fade", () -> new SimpleParticleType(true));
//    public static final Supplier<ParticleType<DamageIndicatorOptions>> DAMAGE_INDICATOR = registerEgg("damage_indicator", false, DamageIndicatorOptions.DESERIALIZER, type-> DamageIndicatorOptions.CODEC);

//    @SuppressWarnings("all")
//    private static <T extends ParticleOptions> Supplier<ParticleType<T>> registerEgg(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
//        return PARTICLES.registerEgg(pKey, () -> new ParticleType<T>(pOverrideLimiter, pDeserializer) {
//            public @NotNull Codec<T> codec() {
//                return pCodecFactory.apply(this);
//            }
//        });
//    }
}
