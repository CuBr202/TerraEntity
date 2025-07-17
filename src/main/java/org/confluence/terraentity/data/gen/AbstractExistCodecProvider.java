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
import oshi.util.tuples.Pair;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractExistCodecProvider<T> implements DataProvider {
    protected PackOutput output;
    private  final List<Pair<ResourceLocation, T>> jsons;
    private final List<CompletableFuture<?>> futures;
    private final Gson gson;
    protected CompletableFuture<HolderLookup.Provider> lookupProvider;
    public AbstractExistCodecProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.jsons = new ArrayList<>();
        this.futures = new ArrayList<>();
        this.lookupProvider = lookupProvider;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    protected abstract void run(HolderLookup.Provider provider);

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        return this.lookupProvider.thenCompose(provider -> {
            run(provider);
            this.jsons.forEach(pair -> {
                this.futures.add(DataProvider.saveStable(cachedOutput,  decode(pair.getB(), provider), getPath(pair.getA())));
            });
            return CompletableFuture.allOf(this.futures.toArray(CompletableFuture[]::new));
        });
    }

    protected abstract Codec<T> getCodec();

    protected void gen(ResourceLocation location, T checkPoint){
        this.jsons.add(new Pair<>(location, checkPoint));
    }

    protected Path getPath(ResourceLocation loc) {
        return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(loc.getNamespace()).resolve(loc.getPath() + ".json");
    }

    protected JsonElement parseCodec(DataResult<?> result){
        return JsonParser.parseString(gson.toJson(result.result().get()));
    }

    protected JsonObject decode(T checkPoint, HolderLookup.Provider provider){
        return parseCodec(getCodec().encodeStart(JsonOps.INSTANCE, checkPoint)).getAsJsonObject();
    }
}
