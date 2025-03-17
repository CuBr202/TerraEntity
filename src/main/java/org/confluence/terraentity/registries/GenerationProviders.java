package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.proj.generation.GenerationProvider;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

/**
 * 跟踪类型编解码器注册表
 */
public class GenerationProviders extends DeferredRegister<GenerationProvider> {
    public static final ResourceKey<Registry<GenerationProvider>> GENERATION_PROVIDER_KEY = createRegistryKey(TerraEntity.space("generation_provider"));
    public static final Registry<GenerationProvider> GENERATION_PROVIDER_REGISTRY = new RegistryBuilder<>(GENERATION_PROVIDER_KEY).create();
    protected GenerationProviders(String namespace) {
        super(GENERATION_PROVIDER_KEY, namespace);
    }

    public static GenerationProviders create(String mod_id) {
        return new GenerationProviders(mod_id);
    }
}
