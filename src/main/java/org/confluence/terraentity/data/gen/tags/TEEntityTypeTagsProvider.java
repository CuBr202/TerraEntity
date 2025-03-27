package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TEEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TEEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, TerraEntity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Stream.of(TETags.EntityTypes.SLIME, EntityTypeTags.NON_CONTROLLING_RIDER)
                .forEach(type -> tag(type).add(
                        TEEntities.BLUE_SLIME.get(),
                        TEEntities.GREEN_SLIME.get(),
                        TEEntities.PINK_SLIME.get(),
                        TEEntities.CORRUPTED_SLIME.get(),
                        TEEntities.DESERT_SLIME.get(),
                        TEEntities.JUNGLE_SLIME.get(),
                        TEEntities.EVIL_SLIME.get(),
                        TEEntities.ICE_SLIME.get(),
                        TEEntities.LAVA_SLIME.get(),
                        TEEntities.LUMINOUS_SLIME.get(),
                        TEEntities.CRIMSON_SLIME.get(),
                        TEEntities.PURPLE_SLIME.get(),
                        TEEntities.RED_SLIME.get(),
                        TEEntities.TROPIC_SLIME.get(),
                        TEEntities.YELLOW_SLIME.get(),
                        TEEntities.HONEY_SLIME.get(),
                        TEEntities.BLACK_SLIME.get(),
                        EntityType.SLIME)
                );

        EntityType<?>[] bosses = {
                TEEntities.EYE_OF_CTHULHU.get(),
                TEEntities.KING_SLIME.get(),
                TEEntities.EATER_OF_WORLDS.get(),
                TEEntities.EATER_OF_WORLD_SEGMENT.get(),
                TEEntities.BRAIN_OF_CTHULHU.get(),
                TEEntities.QUEEN_BEE.get(),
                TEEntities.SKELETRON.get(),
                TEEntities.SKELETRON_HAND.get()
        };
        tag(Tags.EntityTypes.BOSSES).add(bosses);
        tag(TagKey.create(Registries.ENTITY_TYPE, TerraEntity.fromSpaceAndPath("ars_nouveau", "jar_blacklist"))).add(bosses);
    }

}
