package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.mood.Mood;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NPCMoodProvider extends AbstractExistCodecProvider<Map<EntityType<?>, NPCMoods.EntityMood>> {

    public NPCMoodProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void run(HolderLookup.Provider provider) {
        Map<EntityType<?>, NPCMoods.EntityMood> map = new HashMap<>();
        // goblin_tinkerer
        map.put(TENpcEntities.GOBLIN_TINKERER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("goblin1"), MoodInfo.of(TENpcEntities.DYE_TRADER.get(), "mood.terra_entity.goblin_tinkerer.like.dye_trader", Mood.LIKE))
                .build());

        // guide
        map.put(TENpcEntities.GUIDE.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("guild1"), MoodInfo.of(TENpcEntities.PAINTER.get(), "mood.terra_entity.guide.hate.painter", Mood.HATE))
                .build());

        // arms_dealer
        map.put(TENpcEntities.ARMS_DEALER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("arms_dealer1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "mood.terra_entity.arms_dealer.hate.demolitionist", Mood.HATE))
                .addMoodInfo(TerraEntity.space("arms_dealer2"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.arms_dealer.love.nurse", Mood.LOVER))
                .build());

        // angler
        map.put(TENpcEntities.ANGLER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("angler1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "mood.terra_entity.angler.like.demolitionist", Mood.LIKE))
                .build());

        // dye_trader
        map.put(TENpcEntities.DYE_TRADER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dye_trader1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.dye_trader.like.arms_dealer", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("dye_trader2"), MoodInfo.of(TENpcEntities.PAINTER.get(), "mood.terra_entity.dye_trader.like.painter", Mood.LIKE))
                .build());

        // demolitionist
        map.put(TENpcEntities.DEMOLITIONIST.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("demolitionist1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.demolitionist.dislike.arms_dealer", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("demolitionist2"), MoodInfo.of(TENpcEntities.GOBLIN_TINKERER.get(), "mood.terra_entity.demolitionist.dislike.goblin_tinkerer", Mood.DISLIKE))
                .build());

        // painter
        map.put(TENpcEntities.PAINTER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("painter1"), MoodInfo.of(TENpcEntities.DRYAD.get(), "mood.terra_entity.painter.love.dryad", Mood.LOVER))
                .build());

        // dryad
        map.put(TENpcEntities.DRYAD.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dryad1"), MoodInfo.of(TENpcEntities.ANGLER.get(), "mood.terra_entity.dryad.dislike.angler", Mood.DISLIKE))
                .build());

        // merchant
        map.put(TENpcEntities.MERCHANT.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("merchant1"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.merchant.like.nurse", Mood.LIKE))
                .build());

        // nurse
        map.put(TENpcEntities.NURSE.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("nurse1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.nurse.love.rms_dealer", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("nurse2"), MoodInfo.of(TENpcEntities.DRYAD.get(), "mood.terra_entity.nurse.dislike.dryad", Mood.DISLIKE))
                .build());

        gen(TerraEntity.space(NPCMoods.FILE_NAME), map);
    }

    @Override
    protected Codec<Map<EntityType<?>, NPCMoods.EntityMood>> getCodec() {
        return NPCMoods.MAP_LIST_CODEC;
    }

    @Override
    public String getName() {
        return NPCMoods.KEY;
    }
}
