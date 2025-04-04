package org.confluence.terraentity.init.entity;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.summon.SummonHornet;
import org.confluence.terraentity.entity.summon.SummonIronGolem;
import org.confluence.terraentity.entity.summon.SummonSlime;
import org.confluence.terraentity.init.TEEntities;

public class TESummonEntities {
    // tip 召唤物
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSlime>> SUMMON_SLIME = TEEntities.registerEntity("slime_baby", SummonSlime::new ,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonIronGolem>> SUMMON_IRON_GOLEM = TEEntities.registerEntity("i_32_iron_golem", SummonIronGolem::new,1.5F,3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonHornet>> SUMMON_HORNET = TEEntities.registerEntity("hornet_baby", SummonHornet::new,0.5F,0.8F);

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        // sommon
        event.registerEntityRenderer(TESummonEntities.SUMMON_SLIME.get(), c-> new GeoNormalRenderer<>(c, TESummonEntities.SUMMON_SLIME.getId().withPrefix("summon/"),false));
        event.registerEntityRenderer(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolemRenderer::new);
        event.registerEntityRenderer(TESummonEntities.SUMMON_HORNET.get(), c->new GeoNormalRenderer<>(c, TEMonsterEntities.HORNET.getId(),true, 0.6f, 0.5f));

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        // sommon
        event.put(TESummonEntities.SUMMON_SLIME.get(), AbstractTerraBossBase.createAttributes().build());
        event.put(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolem.createAttributes().build());
        event.put(TESummonEntities.SUMMON_HORNET.get(), AbstractMonster.createAttributes().build());
    }

    public static void register(){

    }
}
