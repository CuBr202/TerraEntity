package org.confluence.terraentity.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.proj.track.TrackTypeProvider;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

/**
 * 跟踪类型编解码器注册表
 */
public class TrackTypeProviders extends DeferredRegister<TrackTypeProvider> {
    public static final ResourceKey<Registry<TrackTypeProvider>> TRACK_TYPE_PROVIDER_KEY = createRegistryKey(TerraEntity.space("track_type_provider"));
    public static final Registry<TrackTypeProvider> TRACK_TYPE_PROVIDER_REGISTRY = new RegistryBuilder<>(TRACK_TYPE_PROVIDER_KEY).create();
    protected TrackTypeProviders(String namespace) {
        super(TRACK_TYPE_PROVIDER_KEY, namespace);
    }

    public static TrackTypeProviders create(String mod_id) {
        return new TrackTypeProviders(mod_id);
    }
}
