package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BiomeTypeTagsProvider extends BiomeTagsProvider {

    public BiomeTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, TerraEntity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {



    }

}
