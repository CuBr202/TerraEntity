package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.npc.AbstractTerraNPC;
import org.confluence.terraentity.init.TEEntities;

public class TENpcEntities {


    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> GUIDE = TEEntities.registerEntity("guide", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> DEMOLITIONIST = TEEntities.registerEntity("demolitionist", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);
    public static final DeferredHolder<EntityType<?>, EntityType<AbstractTerraNPC>> GOBLIN_TINKERER = TEEntities.registerEntity("goblin_tinkerer", AbstractTerraNPC::new, MobCategory.CREATURE, 0.6f, 1.85f);


    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(GUIDE.get(), c -> new GeoNormalRenderer<>(c, GUIDE.getId().withPrefix("npc/")));
        event.registerEntityRenderer(DEMOLITIONIST.get(), c -> new GeoNormalRenderer<>(c, DEMOLITIONIST.getId().withPrefix("npc/")));
        event.registerEntityRenderer(GOBLIN_TINKERER.get(), c -> new GeoNormalRenderer<>(c, GOBLIN_TINKERER.getId().withPrefix("npc/")));
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GUIDE.get(), AbstractTerraNPC.createAttributes().build());
        event.put(DEMOLITIONIST.get(), AbstractTerraNPC.createAttributes().build());
        event.put(GOBLIN_TINKERER.get(), AbstractTerraNPC.createAttributes().build());
    }

    public static void register(){

    }
}
