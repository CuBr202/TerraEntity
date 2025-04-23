package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.mood.Mood;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMoods;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.HashMap;
import java.util.Map;

public class NPCMoodProvider extends AbstractExistCodecProvider<Map<EntityType<?>, NPCMoods.EntityMood>> {

    public NPCMoodProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void run() {
        Map<EntityType<?>, NPCMoods.EntityMood> map = new HashMap<>();
        // goblin_tinkerer
        map.put(TENpcEntities.GOBLIN_TINKERER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("goblin1"), MoodInfo.of(TENpcEntities.DYE_TRADER.get(), "like_dye_trader", Mood.LIKE))
                .build());

        // guide
        map.put(TENpcEntities.GUIDE.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("guild1"), MoodInfo.of(TENpcEntities.PAINTER.get(), "fuck_painter", Mood.HATE))
                .build());

        // arms_dealer
        map.put(TENpcEntities.ARMS_DEALER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("arms_dealer1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "fuck_demolitionist", Mood.HATE))
                .addMoodInfo(TerraEntity.space("arms_dealer2"), MoodInfo.of(TENpcEntities.NURSE.get(), "love_nurse", Mood.LOVER))
                .build());

        // angler
        map.put(TENpcEntities.ANGLER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("angler1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "like_demolitionist", Mood.LIKE))
                .build());

        // dye_trader
        map.put(TENpcEntities.DYE_TRADER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dye_trader1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "like_arms_dealer", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("dye_trader2"), MoodInfo.of(TENpcEntities.PAINTER.get(), "like_painter", Mood.LIKE))
                .build());

        // demolitionist
        map.put(TENpcEntities.DEMOLITIONIST.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("demolitionist1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "dislike_arms_dealer", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("demolitionist2"), MoodInfo.of(TENpcEntities.GOBLIN_TINKERER.get(), "dislike_goblin_tinkerer", Mood.DISLIKE))
                .build());

        // painter
        map.put(TENpcEntities.PAINTER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("painter1"), MoodInfo.of(TENpcEntities.DRYAD.get(), "love_dryad", Mood.LOVER))
                .build());

        // dryad
        map.put(TENpcEntities.DRYAD.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dryad1"), MoodInfo.of(TENpcEntities.ANGLER.get(), "dislike_angler", Mood.DISLIKE))
                .build());

        // merchant
        map.put(TENpcEntities.MERCHANT.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("merchant1"), MoodInfo.of(TENpcEntities.NURSE.get(), "like_nurse", Mood.LIKE))
                .build());

        // nurse
        map.put(TENpcEntities.NURSE.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("nurse1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "love_arms_dealer", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("nurse2"), MoodInfo.of(TENpcEntities.DRYAD.get(), "dislike_dryad", Mood.DISLIKE))
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
