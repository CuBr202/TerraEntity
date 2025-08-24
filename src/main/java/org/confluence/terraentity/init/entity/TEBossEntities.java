package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.util.LibUtils;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.boss.model.SkeletronHandModel;
import org.confluence.terraentity.client.boss.model.WallOfFleshEyeModel;
import org.confluence.terraentity.client.boss.renderer.*;
import org.confluence.terraentity.client.entity.renderer.CrownOfKingSlimeModelRenderer;
import org.confluence.terraentity.client.entity.renderer.GeoMotionBlurRenderer;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.client.entity.renderer.mob.KingSlimeRenderer;
import org.confluence.terraentity.entity.blur.PosRotMotionBlurRenderer;
import org.confluence.terraentity.entity.boss.*;
import org.confluence.terraentity.entity.boss.hillofflesh.HillOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFlesh;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshEye;
import org.confluence.terraentity.entity.boss.wallofflesh.WallOfFleshMouth;
import org.confluence.terraentity.entity.model.CrownOfKingSlimeModelEntity;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.util.AttBuilder;
import org.confluence.terraentity.init.TEEntities;

public class TEBossEntities {

    public static final DeferredHolder<EntityType<?>, EntityType<KingSlime>> KING_SLIME = TEEntities.ENTITIES.register("king_slime", () -> EntityType.Builder.<KingSlime>of(KingSlime::new, MobCategory.MONSTER).sized(0.6f, 0.6f).clientTrackingRange(10).build(TEEntities.Key("king_slime")));
    public static final DeferredHolder<EntityType<?>, EntityType<CrownOfKingSlimeModelEntity>> CROWN_OF_KING_SLIME_MODEL = TEEntities.ENTITIES.register("crown_of_king_slime_model", () -> EntityType.Builder.<CrownOfKingSlimeModelEntity>of(CrownOfKingSlimeModelEntity::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(10).build(TEEntities.Key("crown_of_king_slime_model")));
    public static final DeferredHolder<EntityType<?>, EntityType<EyeOfCthulhu>> EYE_OF_CTHULHU = TEEntities.registerMonster("eye_of_cthulhu", EyeOfCthulhu::new, 2.04F, 2.04F);
    public static final DeferredHolder<EntityType<?>, EntityType<EaterOfWorldsSegment>> EATER_OF_WORLDS_SEGMENT = TEEntities.registerMonster("eater_of_worlds_segment", EaterOfWorldsSegment::new, 2F, 2F);
    public static final DeferredHolder<EntityType<?>, EntityType<EaterOfWorlds>> EATER_OF_WORLDS = TEEntities.registerMonster("eater_of_worlds", EaterOfWorlds::new, 3F, 2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BrainOfCthulhu>> BRAIN_OF_CTHULHU = TEEntities.registerMonster("brain_of_cthulhu", BrainOfCthulhu::new, 4F, 4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BrainFake>> BRAIN_FAKE = TEEntities.registerMonster("brain_fake", BrainFake::new, 4F, 4F);
    public static final DeferredHolder<EntityType<?>, EntityType<QueenBee>> QUEEN_BEE = TEEntities.registerMonster("queen_bee", QueenBee::new, 2.5F, 3F);
    public static final DeferredHolder<EntityType<?>, EntityType<Skeletron>> SKELETRON = TEEntities.registerMonster("skeletron", Skeletron::new, 2.3F, 2.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SkeletronHand>> SKELETRON_HAND = TEEntities.registerMonster("skeletron_hand", SkeletronHand::new, 2F, 2F);
    public static final DeferredHolder<EntityType<?>, EntityType<WallOfFlesh>> WALL_OF_FLESH = TEEntities.registerMonster("wall_of_flesh", WallOfFlesh::new, 0.1F,0.1F);
    public static final DeferredHolder<EntityType<?>, EntityType<WallOfFleshEye>> WALL_OF_FLESH_EYE = TEEntities.registerMonster("wall_of_flesh_eye", WallOfFleshEye::new, 4.15F, 4.15F);
    public static final DeferredHolder<EntityType<?>, EntityType<WallOfFleshMouth>> WALL_OF_FLESH_MOUTH = TEEntities.registerMonster("wall_of_flesh_mouth", WallOfFleshMouth::new, 4.15F, 4.15F);
    public static final DeferredHolder<EntityType<?>, EntityType<DungeonGuardian>> DUNGEON_GUARDIAN = TEEntities.registerMonster("dungeon_guardian", DungeonGuardian::new, 2.5F, 2.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<HillOfFlesh>> HILL_OF_FLESH = TEEntities.registerMonster("hill_of_flesh", HillOfFlesh::new, 10F,10F);


    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TEBossEntities.CROWN_OF_KING_SLIME_MODEL.get(), CrownOfKingSlimeModelRenderer::new);

        event.registerEntityRenderer(TEBossEntities.KING_SLIME.get(), KingSlimeRenderer::new);
        event.registerEntityRenderer(TEBossEntities.EYE_OF_CTHULHU.get(), c->new GeoMotionBlurRenderer<>(c,new GeoBossModel<>(TEBossEntities.EYE_OF_CTHULHU), true,1,0.5f).setMotionBlurRenderer(r->new PosRotMotionBlurRenderer<>(r.isIfRotX(), r.getOffsetY())));
        event.registerEntityRenderer(TEBossEntities.EATER_OF_WORLDS_SEGMENT.get(), c-> new EaterOfWorldSegmentRenderer(c,2.2f, 0f));
        event.registerEntityRenderer(TEBossEntities.EATER_OF_WORLDS.get(), c->new GeoNormalRenderer<>(c,new GeoBossModel<>(TEBossEntities.EATER_OF_WORLDS), true,2.2f,0));
        event.registerEntityRenderer(TEBossEntities.BRAIN_OF_CTHULHU.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(TEBossEntities.BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(TEMonsterEntities.VISUAL_NEURON.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.VISUAL_NEURON.getId(),true));
        event.registerEntityRenderer(TEBossEntities.BRAIN_FAKE.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(TEBossEntities.BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(TEBossEntities.QUEEN_BEE.get(), c->new QueenBeeRenderer(c,new GeoBossModel<>(TEBossEntities.QUEEN_BEE)));
        event.registerEntityRenderer(TEBossEntities.SKELETRON.get(), c->new SkeletronRenderer(c,new GeoBossModel<>(TEBossEntities.SKELETRON)));
        event.registerEntityRenderer(TEBossEntities.SKELETRON_HAND.get(), c->new SkeletronHandRenderer(c,new SkeletronHandModel()));
        event.registerEntityRenderer(TEBossEntities.DUNGEON_GUARDIAN.get(), c->new SkeletronRenderer(c,new GeoBossModel<>(TEBossEntities.SKELETRON)));

        event.registerEntityRenderer(TEBossEntities.WALL_OF_FLESH.get(), WallOfFleshRenderer::new);
        event.registerEntityRenderer(TEBossEntities.WALL_OF_FLESH_EYE.get(), c->new GeoNormalRenderer<>(c,new WallOfFleshEyeModel(WALL_OF_FLESH_EYE.getId()), true,2,0.5f));
        event.registerEntityRenderer(TEBossEntities.WALL_OF_FLESH_MOUTH.get(), c->new GeoNormalRenderer<>(c,new GeoBossModel<>(WALL_OF_FLESH_MOUTH), true,2,0.5f));

        event.registerEntityRenderer(TEBossEntities.HILL_OF_FLESH.get(), HillOfFleshRenderer::new);

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {


        event.put(TEBossEntities.KING_SLIME.get(), KingSlime.createSlimeAttributes().build());
        event.put(TEBossEntities.EYE_OF_CTHULHU.get(), AttBuilder.createBoss(728, 12).attack(4).build());
        event.put(TEBossEntities.EATER_OF_WORLDS_SEGMENT.get(), AttBuilder.createBoss(50, 1).build());
        event.put(TEBossEntities.EATER_OF_WORLDS.get(), AttBuilder.createBoss(54, 0).attack(12.5).build());
        event.put(TEBossEntities.BRAIN_OF_CTHULHU.get(), AttBuilder.createBoss(552, 14).attack(5).build());

        event.put(TEBossEntities.BRAIN_FAKE.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TEBossEntities.QUEEN_BEE.get(), LibUtils.forMixin$ModifyExpression(AttBuilder.createBoss(1237, 8).attack(14).build()));
        event.put(TEBossEntities.SKELETRON.get(), LibUtils.forMixin$ModifyExpression(AttBuilder.createBoss(2288, 10).attack(18.2).build()));
        event.put(TEBossEntities.SKELETRON_HAND.get(), LibUtils.forMixin$ModifyExpression(AttBuilder.createBoss(405,4).attack(10).build()));

        event.put(TEBossEntities.WALL_OF_FLESH.get(), AttBuilder.createBoss(3096, 0).attack(39).build());
        event.put(TEBossEntities.WALL_OF_FLESH_EYE.get(), AttBuilder.createBoss(3096, 0).attack(8).followRange(32).build());
        event.put(TEBossEntities.WALL_OF_FLESH_MOUTH.get(), AttBuilder.createBoss(3096, 12).attack(39).followRange(32).build());
        event.put(TEBossEntities.DUNGEON_GUARDIAN.get(), AttBuilder.createBoss(9999,9999).attack(9999).build());

        event.put(TEBossEntities.HILL_OF_FLESH.get(), AttBuilder.createBoss(3096, 0).build());

    }

    public static void register(){

    }
}
