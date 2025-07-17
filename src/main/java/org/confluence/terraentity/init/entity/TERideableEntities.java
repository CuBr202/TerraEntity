package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.client.entity.renderer.GeoNormalRenderer;
import org.confluence.terraentity.entity.monster.AbstractMonster;
import org.confluence.terraentity.entity.rideable.RideableBee;
import org.confluence.terraentity.entity.rideable.RideableSlime;
import org.confluence.terraentity.init.TEEntities;

public class TERideableEntities {

    public static final RegistryObject<EntityType<RideableSlime>> RIDEABLE_SLIME = TEEntities.registerEntity("rideable_slime", RideableSlime::new,0.5F,0.5F);
    public static final RegistryObject<EntityType<RideableBee>> RIDEABLE_BEE = TEEntities.registerEntity("rideable_bee", RideableBee::new,0.5F,0.5F);

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(TERideableEntities.RIDEABLE_SLIME.get(), c->new GeoNormalRenderer<>(c, TERideableEntities.RIDEABLE_SLIME.getId().withPrefix("rideable/"),false, 1.6f,0));
        event.registerEntityRenderer(TERideableEntities.RIDEABLE_BEE.get(), c->new GeoNormalRenderer<>(c, TERideableEntities.RIDEABLE_BEE.getId().withPrefix("rideable/"),false));

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(TERideableEntities.RIDEABLE_SLIME.get(), AbstractMonster.createAttributes().build());
        event.put(TERideableEntities.RIDEABLE_BEE.get(), AbstractMonster.createAttributes().build());

    }
    public static void register() {

    }
}
