package org.confluence.terraentity.init.entity;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.client.entity.model.GeoNormalModel;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.client.entity.renderer.mob.SummonSwordRenderer;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.summon.*;
import org.confluence.terraentity.init.TEEntities;

import java.awt.Color;

public class TESummonEntities {
    // tip 召唤物
    public static final DeferredHolder<EntityType<?>, EntityType<SummonFinch>> SUMMON_FINCH = TEEntities.registerEntity("finch_baby", SummonFinch::new ,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSlime>> SUMMON_SLIME = TEEntities.registerEntity("slime_baby", SummonSlime::new ,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonIronGolem>> SUMMON_IRON_GOLEM = TEEntities.registerEntity("i_32_iron_golem", SummonIronGolem::new,1.5F,3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonHornet>> SUMMON_HORNET = TEEntities.registerEntity("hornet_baby", SummonHornet::new,0.5F,0.8F);

    // 棱镜系列
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSword>> SUMMON_STONE_SWORD = TEEntities.registerEntity("summon_stone_sword", (e,l)->new SummonSword(e,l, ()->Items.STONE_SWORD,0x8E9797 ),1F,1F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSword>> SUMMON_IRON_SWORD = TEEntities.registerEntity("summon_iron_sword", (e,l)->new SummonSword(e,l, ()->Items.IRON_SWORD, 0xE6F0F3),1F,1F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSword>> SUMMON_GOLDEN_SWORD = TEEntities.registerEntity("summon_golden_sword", (e,l)->new SummonSword(e,l, ()->Items.GOLDEN_SWORD, 0xE3D529),1F,1F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSword>> SUMMON_DIAMOND_SWORD = TEEntities.registerEntity("summon_diamond_sword", (e,l)->new SummonSword(e,l, ()->Items.DIAMOND_SWORD, 0x17CFC1),1F,1F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSword>> SUMMON_NETHERITE_SWORD = TEEntities.registerEntity("summon_netherite_sword", (e,l)->new SummonSword(e,l, ()->Items.NETHERITE_SWORD, 0x8136D2),1F,1F);

//    Color c = new Color(0x8E9797);  // 删掉注释查看颜色

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        // sommon
        event.registerEntityRenderer(TESummonEntities.SUMMON_FINCH.get(), c-> new GeoNormalRenderer<>(c, TESummonEntities.SUMMON_FINCH.getId().withPrefix("summon/"),true));
        event.registerEntityRenderer(TESummonEntities.SUMMON_SLIME.get(), c-> new GeoNormalRenderer<>(c, TESummonEntities.SUMMON_SLIME.getId().withPrefix("summon/"),false));
        event.registerEntityRenderer(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolemRenderer::new);
        event.registerEntityRenderer(TESummonEntities.SUMMON_HORNET.get(), c->new GeoNormalRenderer<>(c, new GeoNormalModel<>(TEMonsterEntities.HORNET.getId(),false),true, 0.6f, 0.5f));

        event.registerEntityRenderer(TESummonEntities.SUMMON_STONE_SWORD.get(), c->new SummonSwordRenderer<>(c));
        event.registerEntityRenderer(TESummonEntities.SUMMON_IRON_SWORD.get(), c->new SummonSwordRenderer<>(c));
        event.registerEntityRenderer(TESummonEntities.SUMMON_GOLDEN_SWORD.get(), c->new SummonSwordRenderer<>(c));
        event.registerEntityRenderer(TESummonEntities.SUMMON_DIAMOND_SWORD.get(), c->new SummonSwordRenderer<>(c));
        event.registerEntityRenderer(TESummonEntities.SUMMON_NETHERITE_SWORD.get(), c->new SummonSwordRenderer<>(c));
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        // sommon
        event.put(TESummonEntities.SUMMON_FINCH.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_SLIME.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_IRON_GOLEM.get(), IronGolem.createAttributes().build());
        event.put(TESummonEntities.SUMMON_HORNET.get(), AbstractMonster.createAttributes().build());

        event.put(TESummonEntities.SUMMON_STONE_SWORD.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_IRON_SWORD.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_GOLDEN_SWORD.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_DIAMOND_SWORD.get(), AbstractMonster.createAttributes().build());
        event.put(TESummonEntities.SUMMON_NETHERITE_SWORD.get(), AbstractMonster.createAttributes().build());
    }

    public static void register(){

    }
}
