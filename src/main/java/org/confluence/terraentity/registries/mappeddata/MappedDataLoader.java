package org.confluence.terraentity.registries.mappeddata;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.confluence.terraentity.TerraEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 热重载注册表的值
 */
public class MappedDataLoader extends SimpleJsonResourceReloadListener {
    public static final String KEY = "mapped_data";

    public MappedDataLoader() {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), KEY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        resourceLocationJsonElementMap.forEach((location, jsonElement) -> {
            String path = location.getPath();
            MappedDataType type = MappedDataTypes.REGISTRY.get().getValue(location.withPath(path.substring(0, path.length() - 5)));
            if(type != null){
                DataResult<MappedData<?>> data = MappedData.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if(data.error().isPresent()){
                    TerraEntity.LOGGER.error("Error parsing {}: {}", location, data.error().get().message());
                }else{
                    type.updateData(data.getOrThrow(true, TerraEntity.LOGGER::error));
                }
            }
        });

    }

    @Override
    public @NotNull String getName() {
        return "Mapped Data Reloader";
    }
}
