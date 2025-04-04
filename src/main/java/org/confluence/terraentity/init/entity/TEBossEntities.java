package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.entity.boss.*;
import org.confluence.terraentity.entity.model.CrownOfKingSlimeModelEntity;
import org.confluence.terraentity.init.TEEntities;

public class TEBossEntities {
    // tip Boss
    public static final RegistryObject<EntityType<KingSlime>> KING_SLIME = TEEntities.ENTITIES.register("king_slime", () -> EntityType.Builder.<KingSlime>of(KingSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10).build(TEEntities.Key("king_slime")));
    public static final RegistryObject<EntityType<CrownOfKingSlimeModelEntity>> CROWN_OF_KING_SLIME_MODEL = TEEntities.ENTITIES.register("crown_of_king_slime_model", () -> EntityType.Builder.<CrownOfKingSlimeModelEntity>of(CrownOfKingSlimeModelEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(10).build(TEEntities.Key("crown_of_king_slime_model")));
    public static final RegistryObject<EntityType<EyeOfCthulhu>> EYE_OF_CTHULHU = TEEntities.registerEntity("eye_of_cthulhu", EyeOfCthulhu::new, 2.04F, 2.04F);
    public static final RegistryObject<EntityType<EaterOfWorldsSegment>> EATER_OF_WORLD_SEGMENT = TEEntities.registerEntity("eater_of_worlds_segment", EaterOfWorldsSegment::new, 2F, 2F);
    public static final RegistryObject<EntityType<EaterOfWorlds>> EATER_OF_WORLDS = TEEntities.registerEntity("eater_of_worlds", EaterOfWorlds::new, 3F, 2F);
    public static final RegistryObject<EntityType<BrainOfCthulhu>> BRAIN_OF_CTHULHU = TEEntities.registerEntity("brain_of_cthulhu", BrainOfCthulhu::new, 4F, 4F);
    public static final RegistryObject<EntityType<BrainFake>> BRAIN_FAKE = TEEntities.registerEntity("brain_fake", BrainFake::new, 4F, 4F);
    public static final RegistryObject<EntityType<QueenBee>> QUEEN_BEE = TEEntities.registerEntity("queen_bee", QueenBee::new, 2.5F, 2.5F);
    public static final RegistryObject<EntityType<Skeletron>> SKELETRON = TEEntities.registerEntity("skeletron", Skeletron::new, 2.3F, 2.3F);
    public static final RegistryObject<EntityType<SkeletronHand>> SKELETRON_HAND = TEEntities.registerEntity("skeletron_hand", SkeletronHand::new, 2F, 1F);
}
