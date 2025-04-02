package org.confluence.terraentity.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.ReactToBell;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

/**
 * 抓人草
 */
public class Snatcher extends AbstractMonster{

    Vec3 initPos;
    Vec3 initDir;
    boolean triggered = false;
    int phase = 0;
    int _phase = 200;

    float forwardSpeed = 0.2f; // 向前速度
    float forwardFreq = 0.05f; // 向前频率
    float backSpeed = 0.1f; // 返回起始点速度
    float backLen = 5;  // 距离起始点方向的距离
    float v_speed = 5f;   // 回到起始方向的速度

    private static final EntityDataAccessor<Vector3f> DATA_TRIGGER =  SynchedEntityData.defineId(Snatcher.class, EntityDataSerializers.VECTOR3);


    public Snatcher(EntityType<? extends Monster> type, Level level, Builder builder) {
        super(type, level, builder);
        this.noPhysics = true;
        this.collisionProperties = new CollisionProperties(5,20,0.3f);
    }

    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));

    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return distanceToSqr(entity) < 32 * 32;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void tick(){
        super.tick();
//        if(initPos != null && !initPos.equals(Vec3.ZERO)){
//            this.setPos(initPos.x, initPos.y + Math.sin(this.tickCount * 0.2f) + 3, initPos.z);
//        }
        phase++;
        int stage = (int) (phase * 2.0F / _phase  + 1);
        if(phase >= _phase){
            phase = 0;
        }
        if(initPos!= null && initDir!= null){


            Vec3 speed = Vec3.ZERO;

            if(getTarget() != null ) {
                Vec3 targetPos = getTarget().position().add(0,getTarget().getEyeHeight() * 0.5f,0);

                this.lookControl.setLookAt(getTarget(), 200, 85);
                this.lookAt(getTarget(), 200, 85);
                // 回复到方向的垂直速度
                Vec3 c = targetPos.subtract(this.initPos);
                Vec3 a = initPos.subtract(position());
                Vec3 b = targetPos.subtract(position());

                float sp = v_speed;
                if(stage == 2){
                    sp = v_speed * 0.05f;
                }
                double s = a.dot(b);
                double c_len = initDir.subtract(targetPos).length();
                double v_len = s / c_len;
                Vec3 v_v = a.cross(b).cross(c).normalize().scale(-v_len * sp );
                speed = speed.add(v_v);
                // 朝向目标的速度
                initDir = c.normalize();

            }
//            Vec3 forward = initDir.normalize().scale(forwardSpeed * Math.sin(this.tickCount * forwardFreq));
//            // 回复到初始位置的速度
//            Vec3 backPos = initPos.add(initDir.scale(backLen));
//            Vec3 v_back = backPos.subtract(position()).scale(backSpeed);

            float flag = getTarget() == null? 1 : 2;
            Vec3 forward = initDir.normalize().scale(forwardSpeed * Math.sin(this.tickCount * forwardFreq * flag));
            // 回复到初始位置的速度
            float lengthFactor = getTarget() == null? 1 : stage;
            Vec3 backPos = initPos.add(initDir.scale(backLen * lengthFactor * 0.5f * (2 + (Math.sin(this.tickCount * 0.05 * flag)))));
            Vec3 v_back = backPos.subtract(position()).scale(backSpeed);

            this.setDeltaMovement(speed.add(forward).add(v_back));

        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TRIGGER, new Vector3f(0,0,0));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("initPos")) {
            Vector3f initPos = new Vector3f(tag.getFloat("initPosX"), tag.getFloat("initPosY"), tag.getFloat("initPosZ"));
            this.entityData.set(DATA_TRIGGER, initPos);
            this.initPos = new Vec3(initPos);
        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if(initPos!= null) {
            tag.putFloat("initPosX", (float) initPos.x);
            tag.putFloat("initPosY", (float) initPos.y);
            tag.putFloat("initPosZ", (float) initPos.z);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key == DATA_TRIGGER && level().isClientSide){
            this.initPos = new Vec3(this.entityData.get(DATA_TRIGGER));
        }
    }

    @Override
    public void onAddedToWorld(){
        super.onAddedToWorld();
        if(!level().isClientSide) {
            Vec3 dir = new Vec3(this.random.nextFloat() - 0.5f, this.random.nextFloat()- 0.5f, this.random.nextFloat()- 0.5f);
            Vec3 vec3 = this.position();
            Vec3 vec31 = vec3.add(dir.scale(50));
            BlockHitResult result = level().clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));

            if (result.getType() == BlockHitResult.Type.BLOCK) {
                Vec3 hitDir = Vec3.atLowerCornerOf(result.getDirection().getNormal()).scale(-0.5f);
                setInitPos(result.getBlockPos().getCenter().add(hitDir).toVector3f());
                this.initDir = dir.normalize();
            }else{
                this.discard();
            }
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(10);
    }

    public Vec3 getInitPos(){
        return initPos;
    }

    public void setInitPos(Vector3f initPos){
        this.entityData.set(DATA_TRIGGER, initPos);
        this.initPos = new Vec3(initPos);
        this.triggered = true;
    }

}
