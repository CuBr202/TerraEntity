package org.confluence.terraentity.entity.boss;


import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TESounds;


public class EaterOfWorldsSegment extends AbstractTerraBossBase<EaterOfWorldsSegment> {
    private static final float MAX_HEALTHS = 50f;
    private static final float DAMAGE = 4f;//接触伤害

    public float segmentInternal = 2f;
    public EaterOfWorlds head;
    public AbstractTerraBossBase lastSegment;

    public boolean ifTail = false;
    public static final EntityDataAccessor<Boolean> DATA_TAIL = SynchedEntityData.defineId(EaterOfWorldsSegment.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TAIL, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
         if(key == DATA_TAIL){
             ifTail = entityData.get(DATA_TAIL);
         }
    }
    public void setHead(EaterOfWorlds head){
        this.head = head;
    }
    public void setLastSegment(AbstractTerraBossBase lastSegment){
        this.lastSegment = lastSegment;
    }

    public EaterOfWorldsSegment(EntityType<? extends Monster> type, Level level) {
        super(type, level,MAX_HEALTHS, 1);
        this.noPhysics = true;
        setAttactDamage(DAMAGE);

        this.xpReward = 30;

    }

    public EaterOfWorldsSegment(EaterOfWorlds head, Level level) {
        this(TEEntities.EATER_OF_WORLD_SEGMENT.get(), level);
        this.head = head;
    }

    public Vec3 getNextPos(){
        if(distanceToSqr(lastSegment)<1f) return position();
        this.lookAt(lastSegment,500,500);
        Vec3 newPos = lastSegment.position().add(position().subtract(lastSegment.position()).normalize().scale(segmentInternal));
        return newPos;
    }

    public boolean isNoGravity(){
        return true;
    }

    @Override
    public void addSkills() { }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean shouldBeSaved(){
        return false;
    }

    int discardTimer = 0;
    @Override
    public void tick(){
        super.tick();

        if(!level().isClientSide) {
            if (lastSegment != null && lastSegment.isAlive())
                this.setPos(getNextPos());
            if(head == null || !head.isAlive() ){
                discardTimer++;
                if(discardTimer > 100) {
                    discard();

                }
                return;
            }
            discardTimer = 0;
        }
    }
    @Override // 受伤音效
    protected SoundEvent getHurtSound(DamageSource damageSource) {return TESounds.ROUTINE_HURT.get();}

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!ForgeHooks.onLivingDeath(this, damageSource)) {
            if (!this.isRemoved() && !this.dead) {
                LivingEntity livingentity = this.getKillCredit();
                if (this.deathScore >= 0 && livingentity != null) {
                    livingentity.awardKillScore(this, this.deathScore, damageSource);
                }
                this.dead = true;
                this.getCombatTracker().recheckStatus();
                Level var5 = this.level();
                if (var5 instanceof ServerLevel) {
                    this.gameEvent(GameEvent.ENTITY_DIE);
                    this.dropAllDeathLoot( damageSource);
                    this.createWitherRose(livingentity);
                    this.level().broadcastEntityEvent(this, (byte)3);
                }
                this.setPose(Pose.DYING);
            }
        }
    }

    public boolean shouldShowBossBar(){return false;};

}
