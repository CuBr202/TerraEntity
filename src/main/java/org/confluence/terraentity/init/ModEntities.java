package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.entity.boss.CthulhuEye;
import org.confluence.terraentity.entity.monster.demoneye.DemonEye;

import static org.confluence.terraentity.TerraEntity.MODID;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<EntityType<?>,EntityType<DemonEye>> DEMON_EYE = ENTITIES.register("demon_eye", () -> EntityType.Builder.of(DemonEye::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(10).build("confluence:demon_eye"));


    public static final DeferredHolder<EntityType<?>,EntityType<CthulhuEye>> CTHULHU_EYE = ENTITIES.register("cthulhu_eye", () -> EntityType.Builder.<CthulhuEye>of(CthulhuEye::new, MobCategory.MONSTER)
            .sized(2.04F, 2.04F)
            .clientTrackingRange(200)
            .setTrackingRange(200)
            .build(MODID+":cthulhu_eye"));



    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder genericBossAttribs = Monster.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.GRAVITY,0)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 1);

        event.put(CTHULHU_EYE.get(), genericBossAttribs.build());




        event.put(ModEntities.DEMON_EYE.get(), DemonEye.createAttributes().build());
    }
}
