package org.confluence.terraentity.init.entity;

import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terraentity.entity.summon.SummonHornet;
import org.confluence.terraentity.entity.summon.SummonIronGolem;
import org.confluence.terraentity.entity.summon.SummonSlime;
import org.confluence.terraentity.init.TEEntities;

public class TESummonEntities {
    // tip 召唤物
    public static final DeferredHolder<EntityType<?>, EntityType<SummonSlime>> SUMMON_SLIME = TEEntities.registerEntity("slime_baby", SummonSlime::new ,0.5F,0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonIronGolem>> SUMMON_IRON_GOLEM = TEEntities.registerEntity("i_32_iron_golem", SummonIronGolem::new,1.5F,3F);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonHornet>> SUMMON_HORNET = TEEntities.registerEntity("hornet_baby", SummonHornet::new,0.5F,0.8F);

    public static void register(){

    }
}
