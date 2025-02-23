package org.confluence.terraentity.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BaseBat extends AbstractMonster{

    public BaseBat(EntityType<? extends Monster> type, Level level, Builder builder) {
        super(type, level, builder);
        
    }


    public void tick() {
        super.tick();



    }

}
