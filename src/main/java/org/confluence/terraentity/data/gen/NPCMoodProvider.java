package org.confluence.terraentity.data.gen;

import com.mojang.serialization.Codec;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.npc.mood.Mood;
import org.confluence.terraentity.entity.npc.mood.MoodInfo;
import org.confluence.terraentity.entity.npc.NPCMoods;
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
        map.put(TENpcEntities.GUIDE.get(), new NPCMoods.EntityMood.Builder()
                        .addMoodInfo(TerraEntity.space("guild1"), MoodInfo.of(TENpcEntities.GOBLIN_TINKERER.get(), "fuck_goblin", Mood.HATE))
                        .addMoodInfo(TerraEntity.space("guild2"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "love_demolition", Mood.LIKE))
                        .build());
        map.put(TENpcEntities.GOBLIN_TINKERER.get(), new NPCMoods.EntityMood.Builder()
                .addMoodInfo(TerraEntity.space("goblin1"), MoodInfo.of(TENpcEntities.DEMOLITIONIST.get(), "fuck_demolitionist", Mood.HATE))
                .addMoodInfo(TerraEntity.space("goblin2"), MoodInfo.of(TENpcEntities.NURSE.get(), "love_nurse", Mood.LIKE))
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
