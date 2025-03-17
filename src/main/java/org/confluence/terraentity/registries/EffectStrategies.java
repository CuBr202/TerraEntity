package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.proj.hit_effect.EffectStrategy;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

/**
 * 命中效果注册表
 */
public class EffectStrategies extends DeferredRegister<EffectStrategy> {
    public static final ResourceKey<Registry<EffectStrategy>> EFFECT_STRATEGY_KEY = createRegistryKey(TerraEntity.asResource("effect_strategy"));
    public static final Registry<EffectStrategy> EFFECT_STRATEGY_REGISTRY = new RegistryBuilder<>(EFFECT_STRATEGY_KEY).create();
    protected EffectStrategies(String namespace) {
        super(EFFECT_STRATEGY_KEY, namespace);
    }

    public static EffectStrategies create(String mod_id) {
        return new EffectStrategies(mod_id);
    }
}
