package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.registries.mappeddata.MappedData;
import org.confluence.terraentity.registries.mappeddata.MappedDataLoader;
import org.confluence.terraentity.registries.mappeddata.MappedDataTypes;

import java.util.concurrent.CompletableFuture;

public class MappedDataProvider extends AbstractExistCodecProvider<MappedData> {

    public MappedDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void run(HolderLookup.Provider provider) {
        MappedDataTypes.TYPES.getEntries().forEach(type -> {
            this.gen(getLocation(type.getId()), type.get().getDefaultValue());
        });
    }

    private ResourceLocation getLocation(ResourceLocation file){
        return TerraEntity.space(MappedDataLoader.KEY).withSuffix("/" + file.getPath()+".json");
    }

    @Override
    protected Codec<MappedData> getCodec() {
        return MappedData.CODEC;
    }

    @Override
    public String getName() {
        return "MappedDataProvider";
    }
}
