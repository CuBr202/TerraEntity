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
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
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
                        TEMonsterEntities.BLUE_SLIME.get(),
                        TEMonsterEntities.GREEN_SLIME.get(),
                        TEMonsterEntities.PINK_SLIME.get(),
                        TEMonsterEntities.CORRUPTED_SLIME.get(),
                        TEMonsterEntities.DESERT_SLIME.get(),
                        TEMonsterEntities.JUNGLE_SLIME.get(),
                        TEMonsterEntities.EVIL_SLIME.get(),
                        TEMonsterEntities.ICE_SLIME.get(),
                        TEMonsterEntities.LAVA_SLIME.get(),
                        TEMonsterEntities.LUMINOUS_SLIME.get(),
                        TEMonsterEntities.CRIMSON_SLIME.get(),
                        TEMonsterEntities.PURPLE_SLIME.get(),
                        TEMonsterEntities.RED_SLIME.get(),
                        TEMonsterEntities.TROPIC_SLIME.get(),
                        TEMonsterEntities.YELLOW_SLIME.get(),
                        TEMonsterEntities.HONEY_SLIME.get(),
                        TEMonsterEntities.BLACK_SLIME.get(),
                        EntityType.SLIME)
                );
        tag(TETags.EntityTypes.CORRUPT).add(
                TEMonsterEntities.EATER_OF_SOULS.get(),
                TEMonsterEntities.DECAYEDER.get(),
                TEMonsterEntities.DEVOURER.get()
        );

        EntityType<?>[] bosses = {
                TEBossEntities.EYE_OF_CTHULHU.get(),
                TEBossEntities.KING_SLIME.get(),
                TEBossEntities.EATER_OF_WORLDS.get(),
                TEBossEntities.EATER_OF_WORLD_SEGMENT.get(),
                TEBossEntities.BRAIN_OF_CTHULHU.get(),
                TEBossEntities.QUEEN_BEE.get(),
                TEBossEntities.SKELETRON.get(),
                TEBossEntities.SKELETRON_HAND.get()
        };
        tag(Tags.EntityTypes.BOSSES).add(bosses);
        tag(TagKey.create(Registries.ENTITY_TYPE, TerraEntity.fromSpaceAndPath("ars_nouveau", "jar_blacklist"))).add(bosses);
    }

}
