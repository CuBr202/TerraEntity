package org.confluence.terraentity.init.entity;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.confluence.terraentity.client.boss.model.GeoBossModel;
import org.confluence.terraentity.client.boss.model.SkeletronHandModel;
import org.confluence.terraentity.client.boss.renderer.*;
import org.confluence.terraentity.client.entity.model.GiantShellyModel;
import org.confluence.terraentity.client.entity.model.NymphModel;
import org.confluence.terraentity.client.entity.renderer.*;

public class TEEntitiesRenderers {
    // tip 渲染器
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TEMonsterEntities.BLUE_SLIME.get(), c -> new CustomSlimeRenderer(c, "blue"));
        event.registerEntityRenderer(TEMonsterEntities.GREEN_SLIME.get(), c -> new CustomSlimeRenderer(c, "green"));
        event.registerEntityRenderer(TEMonsterEntities.PINK_SLIME.get(), c -> new CustomSlimeRenderer(c, "pink"));
        event.registerEntityRenderer(TEMonsterEntities.CORRUPTED_SLIME.get(), c -> new CustomSlimeRenderer(c, "corrupted"));
        event.registerEntityRenderer(TEMonsterEntities.DESERT_SLIME.get(), c -> new CustomSlimeRenderer(c, "desert"));
        event.registerEntityRenderer(TEMonsterEntities.JUNGLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "jungle"));
        event.registerEntityRenderer(TEMonsterEntities.EVIL_SLIME.get(), c -> new CustomSlimeRenderer(c, "evil"));
        event.registerEntityRenderer(TEMonsterEntities.ICE_SLIME.get(), c -> new CustomSlimeRenderer(c, "ice"));
        event.registerEntityRenderer(TEMonsterEntities.LAVA_SLIME.get(), c -> new CustomSlimeRenderer(c, "lava"));
        event.registerEntityRenderer(TEMonsterEntities.LUMINOUS_SLIME.get(), c -> new CustomSlimeRenderer(c, "luminous"));
        event.registerEntityRenderer(TEMonsterEntities.CRIMSON_SLIME.get(), c -> new CustomSlimeRenderer(c, "crimson"));
        event.registerEntityRenderer(TEMonsterEntities.PURPLE_SLIME.get(), c -> new CustomSlimeRenderer(c, "purple"));
        event.registerEntityRenderer(TEMonsterEntities.RED_SLIME.get(), c -> new CustomSlimeRenderer(c, "red"));
        event.registerEntityRenderer(TEMonsterEntities.TROPIC_SLIME.get(), c -> new CustomSlimeRenderer(c, "tropic"));
        event.registerEntityRenderer(TEMonsterEntities.YELLOW_SLIME.get(), c -> new CustomSlimeRenderer(c, "yellow"));
        event.registerEntityRenderer(TEMonsterEntities.HONEY_SLIME.get(), c -> new CustomSlimeRenderer(c, "honey"));
        event.registerEntityRenderer(TEMonsterEntities.BLACK_SLIME.get(), c -> new CustomSlimeRenderer(c, "black"));


        event.registerEntityRenderer(TEMonsterEntities.CRIMSON_KEMERA.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.CRIMSON_KEMERA.getId(),true));
        event.registerEntityRenderer(TEMonsterEntities.EATER_OF_SOULS.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.EATER_OF_SOULS.getId(),true));
        event.registerEntityRenderer(TEMonsterEntities.DRIPPLER.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.DRIPPLER.getId(),false,2f,0));
        event.registerEntityRenderer(TEMonsterEntities.WANDERING_EYE_FISH.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.WANDERING_EYE_FISH.getId(),false,1.5f,0));
        event.registerEntityRenderer(TEMonsterEntities.FLYING_FISH.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.FLYING_FISH.getId(),true,0.75f,0));


        event.registerEntityRenderer(TEMonsterEntities.DEMON_EYE.get(), DemonEyeRenderer::new);
//        if (!ClientConfig.ENABLE_NON_SPIDER_MODEL.get()) {
            event.registerEntityRenderer(TEMonsterEntities.BLOOD_CRAWLER.get(), c -> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_CRAWLER.getId()));
//        }
        event.registerEntityRenderer(TEMonsterEntities.BLOODY_SPORE.get(), BloodySporeRenderer::new);
        event.registerEntityRenderer(TEMonsterEntities.DECAYEDER.get(), DecayederRenderer::new);


        event.registerEntityRenderer(TEMonsterEntities.FACE_MONSTER.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.FACE_MONSTER.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.BLOOD_TUMORS.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_TUMORS.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.BLOOD_ZOMBIE.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.BLOOD_ZOMBIE.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.DEVOURER.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.DEVOURER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.GIANT_WORM.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.GIANT_WORM.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.TOMB_CRAWLER.get(), c-> new GeoWormRenderer<>(c, TEMonsterEntities.TOMB_CRAWLER.getId(),2.0f, 0.0f));
        event.registerEntityRenderer(TEMonsterEntities.GIANT_SHELLY.get(), c-> new GeoNormalRenderer<>(c, new GiantShellyModel<>(TEMonsterEntities.GIANT_SHELLY.getId()),false,2,0));
        // bat
        event.registerEntityRenderer(TEMonsterEntities.CAVE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.CAVE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.JUNGLE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.JUNGLE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.JUNGLE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.JUNGLE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.HELL_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.HELL_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.ICE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.ICE_BAT.getId(),false));
        event.registerEntityRenderer(TEMonsterEntities.SPORE_BAT.get(), c-> new GeoNormalRenderer<>(c, TEMonsterEntities.SPORE_BAT.getId(),false));

        // bee
        event.registerEntityRenderer(TEMonsterEntities.LITTLE_HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.LITTLE_HORNET.getId(),true, 1, 0.5f));
        event.registerEntityRenderer(TEMonsterEntities.HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.HORNET.getId(),true, 1, 0.5f));

        event.registerEntityRenderer(TEMonsterEntities.NYMPH.get(), c->new GeoNormalRenderer<>(c, new NymphModel<>(TEMonsterEntities.NYMPH.getId()),false,1,0f));
        event.registerEntityRenderer(TEMonsterEntities.SNATCHER.get(), c->new SnatcherRenderer<>(c, TEMonsterEntities.SNATCHER.getId()));
        event.registerEntityRenderer(TEMonsterEntities.MAN_EATER.get(), c->new SnatcherRenderer<>(c, TEMonsterEntities.MAN_EATER.getId()));


        // boss
        event.registerEntityRenderer(TEBossEntities.KING_SLIME.get(), KingSlimeRenderer::new);
        event.registerEntityRenderer(TEBossEntities.EYE_OF_CTHULHU.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(TEBossEntities.EYE_OF_CTHULHU),1,0.5f, true));
        event.registerEntityRenderer(TEBossEntities.EATER_OF_WORLD_SEGMENT.get(), c-> new EaterOfWorldSegmentRenderer(c,2.2f, 0f));
        event.registerEntityRenderer(TEBossEntities.EATER_OF_WORLDS.get(), c->new GeoBossRenderer<>(c,new GeoBossModel<>(TEBossEntities.EATER_OF_WORLDS),2.2f,0, true));
        event.registerEntityRenderer(TEBossEntities.BRAIN_OF_CTHULHU.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(TEBossEntities.BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(TEMonsterEntities.VISUAL_NEURON.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.VISUAL_NEURON.getId(),true));
        event.registerEntityRenderer(TEBossEntities.BRAIN_FAKE.get(), c->new BrainOfCthulhuRenderer(c,new GeoBossModel<>(TEBossEntities.BRAIN_OF_CTHULHU)));
        event.registerEntityRenderer(TEBossEntities.QUEEN_BEE.get(), c->new QueenBeeRenderer(c,new GeoBossModel<>(TEBossEntities.QUEEN_BEE)));
        event.registerEntityRenderer(TEBossEntities.SKELETRON.get(), c->new SkeletronRenderer(c,new GeoBossModel<>(TEBossEntities.SKELETRON)));
        event.registerEntityRenderer(TEBossEntities.SKELETRON_HAND.get(), c->new SkeletronHandRenderer(c,new SkeletronHandModel()));
        event.registerEntityRenderer(TEProjectileEntities.SKULL.get(), SkullProjectileRenderer::new);

        // sommon
        event.registerEntityRenderer(TESummonEntities.SUMMON_SLIME.get(), c-> new GeoNormalRenderer<>(c, TESummonEntities.SUMMON_SLIME.getId().withPrefix("summon/"),false));
        event.registerEntityRenderer(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolemRenderer::new);
        event.registerEntityRenderer(TESummonEntities.SUMMON_HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.HORNET.getId(),true, 0.6f, 0.5f));

        // ridable
        event.registerEntityRenderer(TERideableEntities.RIDEABLE_SLIME.get(), c->new GeoNormalRenderer<>(c, TERideableEntities.RIDEABLE_SLIME.getId().withPrefix("rideable/"),false, 1.6f,0));
        event.registerEntityRenderer(TERideableEntities.RIDEABLE_BEE.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.HORNET.getId(),false));


        // 鞭子
        event.registerEntityRenderer(TEProjectileEntities.WHIP_PROJECTILE.get(), WhipEntityRenderer::new);
    }
}
