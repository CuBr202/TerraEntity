package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.entity.TENpcEntities;
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
                .add(TEMonsterEntities.CORRUPT_SLIME.get())
                .add(TEMonsterEntities.DESERT_SLIME.get())
                .add(TEMonsterEntities.JUNGLE_SLIME.get())
                .add(TEMonsterEntities.EVIL_SLIME.get())
                .add(TEMonsterEntities.ICE_SLIME.get())
                .add(TEMonsterEntities.LAVA_SLIME.get())
                .add(TEMonsterEntities.LUMINOUS_SLIME.get())
                .add(TEMonsterEntities.CRIMSLIME.get())
                .add(TEMonsterEntities.PURPLE_SLIME.get())
                .add(TEMonsterEntities.RED_SLIME.get())
                .add(TEMonsterEntities.TROPIC_SLIME.get())
                .add(TEMonsterEntities.YELLOW_SLIME.get())
                .add(TEMonsterEntities.HONEY_SLIME.get())
                .add(TEMonsterEntities.BLACK_SLIME.get())
                .add(EntityType.SLIME);
        tag(TETags.EntityTypes.NON_CONTROLLING_RIDER).add(TagEntry.tag(TETags.EntityTypes.SLIME.location()));

        tag(TETags.EntityTypes.NPC).add(
                TENpcEntities.GUIDE.get(),
                TENpcEntities.DEMOLITIONIST.get(),
                TENpcEntities.GOBLIN_TINKERER.get(),
                TENpcEntities.ARMS_DEALER.get(),
                TENpcEntities.NURSE.get(),
                TENpcEntities.MERCHANT.get(),
                TENpcEntities.PAINTER.get(),
                TENpcEntities.ANGLER.get(),
                TENpcEntities.DRYAD.get(),
                TENpcEntities.DYE_TRADER.get(),
                TENpcEntities.OLD_MAN.get()
        );

        EntityType<?>[] bosses = {
                TEBossEntities.EYE_OF_CTHULHU.get(),
                TEBossEntities.KING_SLIME.get(),
                TEBossEntities.EATER_OF_WORLDS.get(),
                TEBossEntities.EATER_OF_WORLDS_SEGMENT.get(),
                TEBossEntities.BRAIN_OF_CTHULHU.get(),
                TEBossEntities.QUEEN_BEE.get(),
                TEBossEntities.SKELETRON.get(),
                TEBossEntities.SKELETRON_HAND.get()
        };
        tag(Tags.EntityTypes.BOSSES).add(bosses);
        tag(TagKey.create(Registries.ENTITY_TYPE, TerraEntity.fromSpaceAndPath("ars_nouveau", "jar_blacklist"))).add(bosses);
//        tag(EntityTypeTags.ARTHROPOD).add(
//                TERideableEntities.RIDEABLE_BEE.get(),
//                TESummonEntities.SUMMON_HORNET.get(),
//                TEBossEntities.QUEEN_BEE.get(),
//                TEMonsterEntities.HORNET.get(),
//                TEMonsterEntities.LITTLE_HORNET.get(),
//                TEMonsterEntities.GIANT_SHELLY.get()
//        );
        tag(TETags.EntityTypes.FLESH_ALLIANCE).add(
                TEMonsterEntities.LEECH.get(),
                TEMonsterEntities.FLESH_SLIME.get(),
                TEMonsterEntities.THE_HUNGRY.get(),
                TEMonsterEntities.HILL_HUNGRY.get(),
                TEBossEntities.HILL_OF_FLESH.get(),
                TEBossEntities.WALL_OF_FLESH.get()
        );
    }
}
