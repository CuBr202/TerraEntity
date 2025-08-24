package org.confluence.terraentity.entity.boss.hillofflesh;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.boss.AbstractTerraBossBase;
import org.confluence.terraentity.entity.monster.TheHungry;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.entity.monster.slime.FleshSlime;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.TEUtils;

public class HillOfFleshMouse extends HillOfFleshPart {

    int summonHungryInterval = 256;
    TheHungry hungry;

    public HillOfFleshMouse(HillOfFlesh parentMob, String name, float width, float height) {
        super(parentMob, name, width, height);
//        hungry = this.summonHungry(this.modelOffset);
    }

    @Override
    protected void tickPart(double offsetX, double offsetY, double offsetZ, int index) {
        if(!this.level().isClientSide && (this.hungry == null || !this.hungry.isAlive())
                && this.parentMob.tickCount % summonHungryInterval == this.getId() % summonHungryInterval){
            this.hungry = this.summonHungry(this.position());
        }
    }

    private TheHungry summonHungry(Vec3 hungryPos) {
        TheHungry hungry = TEUtils.spawnEntity(() -> new TheHungry(TEMonsterEntities.THE_HUNGRY.get(), level(),
                new AbstractPrefab().getPrefab()) {
            @Override
            protected boolean shouldDropLoot() {
                return false;
            }

            @Override
            protected Vec3 initDirection(LivingEntity owner){
                if(HillOfFleshMouse.this.name.endsWith("0")){
                    return new Vec3(0,0,-1);
                }
                return HillOfFleshMouse.this.modelOffset.add(0,-8,0).normalize().scale(1);
            }
            @Override
            public boolean canAttack(LivingEntity entity) {
                return !(entity.getType().is(TETags.EntityTypes.FLESH_ALLIANCE)) &&  super.canAttack(entity);
            }

        }, (ServerLevel) level(), hungryPos);

        if (hungry != null) {
            hungry.minion_setOwner(this.getParent());
            hungry.setYRot(this.getYRot());
            hungry.setInitPos(this.position().add(0,1,0).toVector3f());
            return hungry;
        }
        return null;
    }

    public void onSummonFleshSlime(FleshSlime fleshSlime){
        fleshSlime.setDeltaMovement(this.modelOffset.normalize().scale(3f));
    }
}
