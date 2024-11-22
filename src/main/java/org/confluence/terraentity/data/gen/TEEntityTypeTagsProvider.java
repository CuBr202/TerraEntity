package org.confluence.terraentity.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TEEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TEEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, TerraEntity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TETags.EntityTypes.SLIME)
                .add(TEEntities.BLUE_SLIME.get())
                .add(TEEntities.GREEN_SLIME.get())
                .add(TEEntities.PINK_SLIME.get())
                .add(TEEntities.CORRUPTED_SLIME.get())
                .add(TEEntities.DESERT_SLIME.get())
                .add(TEEntities.JUNGLE_SLIME.get())
                .add(TEEntities.EVIL_SLIME.get())
                .add(TEEntities.ICE_SLIME.get())
                .add(TEEntities.LAVA_SLIME.get())
                .add(TEEntities.LUMINOUS_SLIME.get())
                .add(TEEntities.CRIMSON_SLIME.get())
                .add(TEEntities.PURPLE_SLIME.get())
                .add(TEEntities.RED_SLIME.get())
                .add(TEEntities.TROPIC_SLIME.get())
                .add(TEEntities.YELLOW_SLIME.get())
                .add(TEEntities.HONEY_SLIME.get())
                .add(TEEntities.BLACK_SLIME.get())
                .add(EntityType.SLIME);
    }

}
