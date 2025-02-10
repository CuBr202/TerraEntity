package org.confluence.terraentity.api.event;


import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;

/**
 * 当BOSS真正死亡时触发，包括和平模式移除时(discard)，蠕虫类只触发一次
 */
public class BossDeathEvent  extends Event implements IModBusEvent {
    AbstractTerraBossBase boss;
    public BossDeathEvent(AbstractTerraBossBase boss) {
        this.boss = boss;
    }
    public AbstractTerraBossBase getBoss() {
        return boss;
    }


}
