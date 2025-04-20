package org.confluence.terraentity.entity.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class SimpleNPC extends AbstractTerraNPC {

    public SimpleNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }


}
