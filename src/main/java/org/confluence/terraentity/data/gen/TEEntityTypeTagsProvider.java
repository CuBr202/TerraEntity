package org.confluence.terraentity.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.entity.EntityType;

import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TEEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TEEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, TerraEntity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TETags.EntityTypes.SLIME)
                .add(TEMonsterEntities.BLUE_SLIME.get())
                .add(TEMonsterEntities.GREEN_SLIME.get())
                .add(TEMonsterEntities.PINK_SLIME.get())
                .add(TEMonsterEntities.CORRUPTED_SLIME.get())
                .add(TEMonsterEntities.DESERT_SLIME.get())
                .add(TEMonsterEntities.JUNGLE_SLIME.get())
                .add(TEMonsterEntities.EVIL_SLIME.get())
                .add(TEMonsterEntities.ICE_SLIME.get())
                .add(TEMonsterEntities.LAVA_SLIME.get())
                .add(TEMonsterEntities.LUMINOUS_SLIME.get())
                .add(TEMonsterEntities.CRIMSON_SLIME.get())
                .add(TEMonsterEntities.PURPLE_SLIME.get())
                .add(TEMonsterEntities.RED_SLIME.get())
                .add(TEMonsterEntities.TROPIC_SLIME.get())
                .add(TEMonsterEntities.YELLOW_SLIME.get())
                .add(TEMonsterEntities.HONEY_SLIME.get())
                .add(TEMonsterEntities.BLACK_SLIME.get())
                .add(EntityType.SLIME);
        tag(TETags.EntityTypes.NON_CONTROLLING_RIDER).add(TagEntry.tag(TETags.EntityTypes.SLIME.location()));
    }

}
