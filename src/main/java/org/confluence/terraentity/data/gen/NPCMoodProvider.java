package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.mood.Mood;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.mood.NPCMood;
import org.confluence.terraentity.init.entity.TENpcEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NPCMoodProvider extends AbstractExistCodecProvider<Map<EntityType<?>, NPCMood.EntityMood>> {

    public NPCMoodProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void run(HolderLookup.Provider provider) {
        Map<EntityType<?>, NPCMood.EntityMood> map = new HashMap<>();
        // goblin_tinkerer
        map.put(TENpcEntities.GOBLIN_TINKERER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("goblin1"), MoodInfo.of(TENpcEntities.DYE_TRADER.get(), "mood.terra_entity.goblin_tinkerer.like.dye_trader", Mood.LIKE))
                .build());

        // guide
        map.put(TENpcEntities.GUIDE.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("guild1"), MoodInfo.of(TENpcEntities.PAINTER.get(), "mood.terra_entity.guide.hate.painter", Mood.HATE))
                .build());

        // arms_dealer
        map.put(TENpcEntities.ARMS_DEALER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("arms_dealer1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "mood.terra_entity.arms_dealer.hate.demolitionist", Mood.HATE))
                .addMoodInfo(TerraEntity.space("arms_dealer2"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.arms_dealer.love.nurse", Mood.LOVER))
                .build());

        // angler
        map.put(TENpcEntities.ANGLER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("angler1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "mood.terra_entity.angler.like.demolitionist", Mood.LIKE))
                .build());
        map.put(TENpcEntities.FEMALE_ANGLER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("female_angler1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "mood.terra_entity.female_angler.dislike.demolitionist", Mood.DISLIKE))
                .build());

        // dye_trader
        map.put(TENpcEntities.DYE_TRADER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dye_trader1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.dye_trader.like.arms_dealer", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("dye_trader2"), MoodInfo.of(TENpcEntities.PAINTER.get(), "mood.terra_entity.dye_trader.like.painter", Mood.LIKE))
                .build());

        // demolitionist
        map.put(TENpcEntities.DEMOLITIONIST.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("demolitionist1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.demolitionist.dislike.arms_dealer", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("demolitionist2"), MoodInfo.of(TENpcEntities.GOBLIN_TINKERER.get(), "mood.terra_entity.demolitionist.dislike.goblin_tinkerer", Mood.DISLIKE))
                .build());

        // painter
        map.put(TENpcEntities.PAINTER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("painter1"), MoodInfo.of(TENpcEntities.DRYAD.get(), "mood.terra_entity.painter.love.dryad", Mood.LOVER))
                .build());

        // dryad
        map.put(TENpcEntities.DRYAD.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("dryad1"), MoodInfo.of(TENpcEntities.ANGLER.get(), "mood.terra_entity.dryad.dislike.angler", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("dryad2"), MoodInfo.of(TENpcEntities.FEMALE_ANGLER.get(), "mood.terra_entity.dryad.like.female_angler", Mood.LIKE))
                .build());

        // merchant
        map.put(TENpcEntities.MERCHANT.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("merchant1"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.merchant.like.nurse", Mood.LIKE))
                .build());

        // nurse
        map.put(TENpcEntities.NURSE.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("nurse1"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.nurse.love.arms_dealer", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("nurse2"), MoodInfo.of(TENpcEntities.DRYAD.get(), "mood.terra_entity.nurse.dislike.dryad", Mood.DISLIKE))
                .build());
        // mechanic
        map.put(TENpcEntities.MECHANIC.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("mechanic1"), MoodInfo.of(TENpcEntities.GOBLIN_TINKERER.get(), "mood.terra_entity.mechanic.love.goblin_tinkerer", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("mechanic2"), MoodInfo.of(TENpcEntities.ARMS_DEALER.get(), "mood.terra_entity.mechanic.dislike.arms_dealer", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("mechanic3"), MoodInfo.of(TENpcEntities.CLOTHIER.get(), "mood.terra_entity.mechanic.hate.clothier", Mood.HATE))
                .build());
                // 喜欢公主,机械侠
        // witch_doctor
        map.put(TENpcEntities.WITCH_DOCTOR.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("witch_doctor1"), MoodInfo.of(TENpcEntities.DRYAD.get(), "mood.terra_entity.witch_doctor.like.dryad", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("witch_doctor1"), MoodInfo.of(TENpcEntities.GUIDE.get(), "mood.terra_entity.witch_doctor.like.guide", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("witch_doctor2"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.witch_doctor.dislike.nurse", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("witch_doctor3"), MoodInfo.of(TENpcEntities.TRUFFLE.get(), "mood.terra_entity.witch_doctor.hate.truffle", Mood.HATE))
                .build());
                // 喜欢公主
        // party_girl
        map.put(TENpcEntities.PARTY_GIRL.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("party_girl1"), MoodInfo.of(TENpcEntities.MERCHANT.get(), "mood.terra_entity.party_girl.dislike.merchant", Mood.DISLIKE))
                .build());
                // 爱巫师，动物学家
                // 讨厌税收官
        // clothier
        map.put(TENpcEntities.CLOTHIER.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("clothier1"), MoodInfo.of(TENpcEntities.TRUFFLE.get(), "mood.terra_entity.clothier.love.truffle", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("clothier2"), MoodInfo.of(TENpcEntities.NURSE.get(), "mood.terra_entity.clothier.dislike.nurse", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("clothier3"), MoodInfo.of(TENpcEntities.MECHANIC.get(), "mood.terra_entity.clothier.hate.mechanic", Mood.HATE))
                .build());
                // 喜欢公主,税收官
        // truffle
        map.put(TENpcEntities.TRUFFLE.get(), new NPCMood.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("truffle1"), MoodInfo.of(TENpcEntities.GUIDE.get(), "mood.terra_entity.truffle.love.guide", Mood.LOVER))
                .addMoodInfo(TerraEntity.space("truffle2"), MoodInfo.of(TENpcEntities.DYE_TRADER.get(), "mood.terra_entity.truffle.like.dye_trader", Mood.LIKE))
                .addMoodInfo(TerraEntity.space("truffle3"), MoodInfo.of(TENpcEntities.CLOTHIER.get(), "mood.terra_entity.truffle.dislike.clothier", Mood.DISLIKE))
                .addMoodInfo(TerraEntity.space("truffle4"), MoodInfo.of(TENpcEntities.WITCH_DOCTOR.get(), "mood.terra_entity.truffle.hate.witch_doctor", Mood.HATE))
                .build());
                // 喜欢公主
        gen(TerraEntity.space("npc/moods"), map);
    }

    @Override
    protected Codec<Map<EntityType<?>, NPCMood.EntityMood>> getCodec() {
        return NPCMood.Loader.CODEC;
    }

    @Override
    public String getName() {
        return "NPC Mood";
    }
}
