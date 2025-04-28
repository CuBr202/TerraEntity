package org.confluence.terraentity.data.gen;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractExistCodecProvider<T> implements DataProvider {
    protected PackOutput output;
    private  final List<tuple> jsons;
    private final List<CompletableFuture<?>> futures;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected CompletableFuture<HolderLookup.Provider> lookupProvider;
    public AbstractExistCodecProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.jsons = new ArrayList<>();
        this.futures = new ArrayList<>();
        this.lookupProvider = lookupProvider;
    }
    private record tuple(JsonObject json, ResourceLocation location) {}

    protected abstract void run();

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        run();
        this.jsons.forEach(pair -> {
            var obj = pair.json;
            Path path = getPath(pair.location);
            this.futures.add(DataProvider.saveStable(cachedOutput, obj, path));
        });
        return CompletableFuture.allOf(this.futures.toArray(CompletableFuture[]::new));
    }

    protected abstract Codec<T> getCodec();

    protected void gen(ResourceLocation location, T checkPoint){
        JsonElement res = parseCodec(getCodec().encodeStart(JsonOps.INSTANCE,checkPoint));
        addJson(res.getAsJsonObject(), location);
    }

    protected void addJson(JsonObject json, ResourceLocation location) {
        this.jsons.add(new tuple(json,location));
    }

    protected Path getPath(ResourceLocation loc) {
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(loc.getNamespace()).resolve(getName()).resolve(loc.getPath() + ".json");
    }

    protected JsonElement parseCodec(DataResult<?> result){
        return JsonParser.parseString(gson.toJson(result.result().get()));
    }
}
