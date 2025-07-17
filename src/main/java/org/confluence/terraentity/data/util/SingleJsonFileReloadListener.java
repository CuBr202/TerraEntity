package org.confluence.terraentity.data.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.confluence.terraentity.TerraEntity;


import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SingleJsonFileReloadListener implements PreparableReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public CompletableFuture<Void> reload(
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
        ResourceLocation resourceLocation = resourcePath();
        for (Resource resource : resourceManager.getResourceStack(resourceLocation)) {
            try (Reader reader = resource.openAsReader()) {
                JsonObject jsonobject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    ResourceLocation loc = ResourceLocation.parse(entry.getKey());
                    map.put(loc, entry.getValue());
                }
            } catch (RuntimeException | IOException ioexception) {
                TerraEntity.LOGGER.error("Couldn't read {} {} in data pack {}", identifier(), resourceLocation, resource.sourcePackId(), ioexception);
            }
        }
        return map;
    }

    protected abstract void apply(Map<ResourceLocation, JsonElement> object);

    protected abstract ResourceLocation resourcePath();

    protected abstract String identifier();
}
