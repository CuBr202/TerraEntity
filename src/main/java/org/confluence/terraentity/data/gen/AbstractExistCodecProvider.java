package org.confluence.terraentity.data.gen;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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
                this.futures.add(saveStable(cachedOutput,  getCodec(), pair.getB(), getPath(pair.getA())));
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

    static AtomicInteger INDENT_WIDTH = new AtomicInteger(2);

    static <T> CompletableFuture<?> saveStable(CachedOutput output, Codec<T> codec, T value, Path path) {
        JsonElement jsonelement = codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow(true, (e)->{
            System.out.println(e);
        });
        return saveStable(output, jsonelement, path);
    }

    static CompletableFuture<?> saveStable(CachedOutput output, JsonElement json, Path path) {
        return CompletableFuture.runAsync(() -> {
            try {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha1(), bytearrayoutputstream);
                JsonWriter jsonwriter = new JsonWriter(new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8));

                try {
                    jsonwriter.setSerializeNulls(false);
                    jsonwriter.setIndent(" ".repeat(Math.max(0, INDENT_WIDTH.get())));
                    GsonHelper.writeValue(jsonwriter, json, KEY_COMPARATOR);
                } catch (Throwable var9) {
                    try {
                        jsonwriter.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }

                    throw var9;
                }

                jsonwriter.close();
                output.writeIfNeeded(path, bytearrayoutputstream.toByteArray(), hashingoutputstream.hash());
            } catch (IOException var10) {
                IOException ioexception = var10;
                LOGGER.error("Failed to save file to {}", path, ioexception);
            }

        }, Util.backgroundExecutor());
    }
}
