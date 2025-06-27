package org.confluence.terraentity.entity.npc.misc;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.utils.TEUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public record NPCNames(Map<String, Float> namesWeights) {
    public static final Codec<NPCNames> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("names_weights").forGetter(NPCNames::namesWeights)
    ).apply(instance, NPCNames::new));

    public static NPCNames of(Map<String, Float> names) {
        return new NPCNames(names);
    }

    public static class Loader extends ContextAwareReloadListener {
        public static final Codec<Map<EntityType<?>, NPCNames>> CODEC = Codec.unboundedMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), NPCNames.CODEC);
        private static Loader INSTANCE;
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        private Map<EntityType<?>, NPCNames> npcNames = ImmutableMap.of();

        @Override
        public final CompletableFuture<Void> reload(
                PreparableReloadListener.PreparationBarrier stage,
                ResourceManager resourceManager,
                ProfilerFiller preparationsProfiler,
                ProfilerFiller reloadProfiler,
                Executor backgroundExecutor,
                Executor gameExecutor
        ) {
            return CompletableFuture.supplyAsync(() -> prepare(resourceManager), backgroundExecutor).thenCompose(stage::wait).thenAcceptAsync(this::apply, gameExecutor);
        }

        protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager) {
            Map<ResourceLocation, JsonElement> map = new HashMap<>();
            ResourceLocation resourceLocation = TerraEntity.space("npc/names.json");
            for (Resource resource : resourceManager.getResourceStack(resourceLocation)) {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject jsonobject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                    for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                        ResourceLocation loc = ResourceLocation.parse(entry.getKey());
                        map.put(loc, entry.getValue());
                    }
                } catch (RuntimeException | IOException ioexception) {
                    TerraEntity.LOGGER.error("Couldn't read npc names {} in data pack {}", resourceLocation, resource.sourcePackId(), ioexception);
                }
            }
            return map;
        }

        protected void apply(Map<ResourceLocation, JsonElement> object) {
            ConditionalOps<JsonElement> ops = makeConditionalOps();
            Map<EntityType<?>, NPCNames> map = new IdentityHashMap<>();
            for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                BuiltInRegistries.ENTITY_TYPE.getOptional(entry.getKey()).ifPresent(entityType -> NPCNames.CODEC.parse(ops, entry.getValue())
                        .resultOrPartial(errorMsg -> TerraEntity.LOGGER.warn("Could not decode npc names with json id {} - error: {}", entry.getKey(), errorMsg))
                        .ifPresent(npcNames -> map.computeIfAbsent(entityType, type -> new NPCNames(new HashMap<>())).namesWeights.putAll(npcNames.namesWeights)));
            }
            ImmutableMap.Builder<EntityType<?>, NPCNames> immutable = ImmutableMap.builder();
            for (Map.Entry<EntityType<?>, NPCNames> entry : map.entrySet()) {
                immutable.put(entry.getKey(), new NPCNames(ImmutableMap.copyOf(entry.getValue().namesWeights)));
            }
            this.npcNames = immutable.build();
        }

        public Map<EntityType<?>, NPCNames> getNpcNames() {
            return npcNames;
        }

        public @Nullable NPCNames getNames(EntityType<?> entityType) {
            return getNpcNames().get(entityType);
        }

        public @Nullable String getRandomName(EntityType<?> entityType) {
            NPCNames names = getNames(entityType);
            if (names == null || names.namesWeights.isEmpty()) {
                return null;
            }
            return TEUtils.getRandomByWeight(names.namesWeights);
        }

        public static Loader getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new Loader();
            }
            return INSTANCE;
        }
    }
}
