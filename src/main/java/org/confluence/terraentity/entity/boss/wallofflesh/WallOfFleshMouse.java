package org.confluence.terraentity.entity.boss.wallofflesh;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terraentity.entity.monster.BaseWorm;
import org.confluence.terraentity.entity.monster.BaseWormPart;
import org.confluence.terraentity.entity.monster.prefab.AbstractPrefab;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.utils.TEUtils;

@SuppressWarnings("all")
public class WallOfFleshMouse extends WallOfFleshPart {

    private static final float DAMAGE = 39f;
    private int pendingSpawns = 0;
    private int spawnInterval = 0;

    private static final int BASE_SUMMON_CD = 100;
    private int summonCDAll = BASE_SUMMON_CD;
    private int summonCD = summonCDAll;


    public WallOfFleshMouse(WallOfFlesh parentMob, String name, float width, float height) {
        super(parentMob, name, width, height);
//        hungry = this.summonHungry(this.modelOffset);
    }
    
    @Override
    public float getYRot() {
        if(this.parentMob!=null)return this.parentMob.getYRot();
        return super.getYRot();
    }

    @Override
    public float getXRot() {
        if(this.parentMob!=null)return this.parentMob.getXRot();
        return super.getXRot();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    protected void tickPart(double offsetX, double offsetY, double offsetZ) {
        if(this.target == null || !this.target.isAlive() || this.parentMob == null || !this.parentMob.isAlive())return;
        if (spawnInterval > 0) {
            if (--spawnInterval <= 0 && pendingSpawns > 0) {
                spawnLeech(target);
                pendingSpawns--;
                spawnInterval = pendingSpawns > 0 ? 10 : 0;
            }
        }

        if (--summonCD <= 0 && this.canShoot(target, 1.0f)) {
            summonCD = summonCDAll + random.nextInt(6) * 20;
            float healthPercent = parentMob.getHealthPercentage();
            int count;
            if (healthPercent > 0.5F) {
                count = 1;
            } else {
                float scaleFactor = Mth.clamp((0.5F - healthPercent) / 0.5F, 0.0F, 1.0F);

                count = 1 + (int) (scaleFactor * 4);
            }
            count = Mth.clamp(count, 1, 5);

            if (pendingSpawns == 0) {
                pendingSpawns = count;
                spawnInterval = 10;
            }
        }
    }

    protected boolean canShoot(Entity target, float range) {
        return target!= null && TEUtils.angleBetween(this.getLookAngle(), target.position().subtract(this.position())) < range;
    }

    private void spawnLeech(LivingEntity target) {
        if (level() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level();
            BaseWorm warm = new BaseWorm(TEMonsterEntities.LEECH.get(), this.level(), AbstractPrefab.WARM_BUILDER.get()){
                @Override
                protected BaseWormPart createPart(int index) {
                    return new BaseWormPart(this, index);
                }

                @Override
                public boolean hurt(DamageSource source, float amount) {
                    if(source.is(DamageTypes.MOB_ATTACK) && source.getEntity().is(WallOfFleshMouse.this))
                        return false;
                    return super.hurt(source, amount);
                }
                @Override
                public boolean canAttack(LivingEntity entity) {
                    return WallOfFleshMouse.this.parentMob.canAttack(entity);
                }
            };
            warm.setPos(position().add(getForward().normalize().scale(1)));
            warm.setTarget(target);
            serverLevel.addFreshEntity(warm);
        }
    }

    @Override
    public boolean isNoGravity(){ return true; }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(WallOfFlesh.isWallOfFleshMob(pSource.getEntity())||WallOfFlesh.isWallOfFleshMob(pSource.getDirectEntity()))
            return false;
        boolean flag = parentMob!=null && parentMob.isAlive();
        return super.hurt(pSource, pAmount) && flag && parentMob.hurt(pSource,pAmount);
    }

    @Override
    public boolean shouldBeSaved(){
        if(this.parentMob != null)return this.parentMob.shouldBeSaved();
        return  false;
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return this.parentMob==null?super.canUsePortal(allowPassengers):this.parentMob.canUsePortal(allowPassengers);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.is(DamageTypeTags.IS_FIRE)||source.is(DamageTypeTags.IS_DROWNING)){
            return true;
        }
        return super.isInvulnerableTo(source);
    }
}