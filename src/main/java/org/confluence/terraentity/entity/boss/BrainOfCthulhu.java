package org.confluence.terraentity.entity.boss;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.entity.ai.Boss;
import org.confluence.terraentity.entity.ai.BossSkill;
import org.confluence.terraentity.entity.ai.motion.curve.Bezier3Curse;
import org.confluence.terraentity.entity.ai.motion.curve.Curve;
import org.confluence.terraentity.entity.monster.FlyEye;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BrainOfCthulhu extends AbstractTerraBossBase implements GeoEntity, Boss {
    private static final float MAX_HEALTHS = 728f;
    private static final float DAMAGE = 1f;//接触伤害
    private static final float MOVE_SPEED = 0.3f;

    private float _moveSpeed = MOVE_SPEED;


    private int minionsCount = 2; // 随从数量
    private final List<FlyEye> minions = new LinkedList<>(); // 随从实体
    private final List<Vec3> homePoses = new ArrayList<>(); // 随从初始位置

    public int stage = 1; //阶段
    private Vec3 inertia;

    private Curve curve;

    public BrainOfCthulhu(EntityType<BrainOfCthulhu> entityType, Level level) {
        super(entityType, level,MAX_HEALTHS);
        //初始属性
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.playSound(TESounds.ROAR.get());
    }

    public BrainOfCthulhu(Level level) {
        this(TEEntities.BRAIN_OF_CTHULHU.get(), level);
    }

    // 定义技能类型
    BossSkill<BrainOfCthulhu> first_spawn;

    BossSkill<BrainOfCthulhu> stage1_stare;
    BossSkill<BrainOfCthulhu> stage1_fade_in;
    BossSkill<BrainOfCthulhu> stage1_fade_out;

    BossSkill<BrainOfCthulhu> switch_1_to_2;
    BossSkill<BrainOfCthulhu> stage2_stare;
    BossSkill<BrainOfCthulhu> stage2_fade_in;
    BossSkill<BrainOfCthulhu> stage2_fade_out;
    BossSkill<BrainOfCthulhu> state2_dash;

    @Override
    public void addSkills() {
        // 定义动画
        RawAnimation close = RawAnimation.begin().thenPlay("close");
        RawAnimation open = RawAnimation.begin().thenPlay("open");
        RawAnimation switching = RawAnimation.begin().thenPlay("to_open");

        first_spawn = new BossSkill<BrainOfCthulhu>(close, 50, 20)
                .onTick(e->{
                    int interval = 2;
                    int cur = skills.tick - 21;
                    if(skills.canContinue() && cur % interval == 0 && minions.size() < minionsCount ){
                        FlyEye minion = TEEntities.FLY_EYE.get().create(level());
                        minions.add(minion);

                        float r = random.nextFloat() + 5;
                        float theta = random.nextFloat() * 2 * (float) Math.PI;
                        float beta = random.nextFloat() * (float) Math.PI;
                        Vec3 pos = TEUtils.sphere(r,theta,beta);

                        homePoses.add(pos);
                        minion.setOwner(this);
                        minion.setPos(position().add(pos));
                        level().addFreshEntity(minion);
                    }
                    setDeltaMovement(0,0.05f,0);
                })
        ;

        stage1_stare = new BossSkill<BrainOfCthulhu>(close, 200, 0)
                .onTick(e->{
                    if(minionsCount <= 0){
                        skills.forceStartIndex(4);
                        stage = 2;
                        return;
                    }

                    if (getTarget() == null) return;

                    if(skills.tick % 20 == 0)
                        for(FlyEye m : minions){
                            if(m.isReady()){
                                m.attack(getTarget());
                                break;
                            }
                        }

                    LookAt(10);

                    // 向玩家正上方移动
                    Vec3 tar = getTarget().position().add(0,3,0);
                    if (distanceToSqr(tar) > 2)
                        setDeltaMovement(tar.subtract(position()).normalize().scale(_moveSpeed / 2));
                })
        ;
        stage1_fade_in = new BossSkill<BrainOfCthulhu>(close, 40, 0)
                .onInit(e->inertia = getDeltaMovement())
                .onTick(e->{
                    this.setDeltaMovement(inertia);
                })
        ;
        stage1_fade_out = new BossSkill<BrainOfCthulhu>(close, 40, 0)
                .onInit(e->{
                    if(getTarget() != null) {
                        float r = random.nextFloat() + 5;
                        float theta = random.nextFloat() * 2 * (float) Math.PI;
                        float beta = random.nextFloat() * (float) Math.PI;
                        Vec3 pos = TEUtils.sphere(r, theta, beta);
                        setPos(getTarget().position().add(pos));
                    }
                })
                .onTick(e->{
                    if(getTarget() == null) return;
                    LookAt(10);
                    // 向玩家正上方移动
                    Vec3 tar = getTarget().position().add(0,3,0);
                    if (distanceToSqr(tar) > 2)
                        setDeltaMovement(tar.subtract(position()).normalize().scale(_moveSpeed / 2));
                })
                .onOver(e->{
                    skills.forceStartIndex(1);
                })
        ;

        switch_1_to_2 = new BossSkill<BrainOfCthulhu>(switching, 20, 0)
                .onTick(e->{
                    LookAt(10);
                })
                .onOver(e->{
                    _moveSpeed = 0.5f;
                    noPhysics = true;
                })
        ;

        stage2_stare = new BossSkill<BrainOfCthulhu>(open, 40, 0)
                .onInit(e->{
                    if(target != null){
                        Vec3 control = target.position().add(random.nextFloat() * 8, 0, random.nextFloat() * 8);
                        Vec3 end = control.add(0,5,0);
                        curve = new Bezier3Curse(position(), control, end);
                    }
                })
                .onTick(e->{
                    setPos(curve.cal(skills.tick / 30f));
                    LookAt(10);
                })
        ;

        state2_dash = new BossSkill<BrainOfCthulhu>(open, 25, 10)
                .onInit(e->{

                })
                .onTick(e->{
                    if(target == null) return;
                    if(skills.canTrigger()){
                        Vec3 control = target.position().add(random.nextFloat() - 0.5f, -2, random.nextFloat() - 0.5f);
                        Vec3 end = control.add(0,5,0);
                        curve = new Bezier3Curse(position(), control, end);
                    }
                    if(skills.canContinue()) {
                        setPos(curve.cal((skills.tick - 10 ) / 15f));
                        LookAt(10);
                    }
                })
        ;

        stage2_fade_in = new BossSkill<BrainOfCthulhu>(open, 30, 0)
                .onTick(e->{
                    if(target != null){
                        Vec3 dir = position().subtract(target.position()).normalize();
                        setDeltaMovement(dir.scale(0.2f));
                    }
                })
        ;
        stage2_fade_out = new BossSkill<BrainOfCthulhu>(open, 30, 0)
                .onInit(e->{
                    if(getTarget() != null) {
                        float r = random.nextFloat() + 5;
                        float theta = random.nextFloat() * 2 * (float) Math.PI;
                        float beta = random.nextFloat() * (float) Math.PI;
                        Vec3 pos = TEUtils.sphere(r, theta, beta);
                        setPos(getTarget().position().add(pos));
                    }
                })
                .onTick(e->{
                    if(getTarget() == null) return;
                    LookAt(10);
                    // 向玩家正上方移动
                    Vec3 tar = getTarget().position().add(0,3,0);
                    if (distanceToSqr(tar) > 2)
                        setDeltaMovement(tar.subtract(position()).normalize().scale(_moveSpeed / 2));
                })
                .onOver(e->{
                    skills.forceStartIndex(5);
                })
        ;


        addSkill(first_spawn);// 0
        addSkill(stage1_stare);//1
        addSkill(stage1_fade_in);//2
        addSkill(stage1_fade_out);

        addSkill(switch_1_to_2);//4

        addSkill(stage2_stare);//5
        addSkill(state2_dash);//6
        addSkill(stage2_fade_in);//7
        addSkill(stage2_fade_out);//8


    }

    public float getFadeProgress(){
        if(skills.index == 2)
            return 1 - (tickCount -lastSkillTick) / 41f;
        else if(skills.index == 3 )
            return (tickCount -lastSkillTick) / 41f;
        else if(skills.index == 7)
            return 1 - (tickCount -lastSkillTick) / 31f;
        else if(skills.index == 8)
            return (tickCount -lastSkillTick) / 31f;

        return 1;
    }

    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof FlyEye);
    }

    public void tick() {
        super.tick();
        if(stage == 1 && !level().isClientSide && tickCount > 50){
            int c = 0;
            for(int i = 0; i < minions.size(); i++){
                FlyEye m = minions.get(i);
                if(m.isAlive()){
                    m.homePos = position().add(homePoses.get(i));
                    c++;
                }
            }
            minionsCount = c;
        }
    }

    @Override // 受伤音效
    protected SoundEvent getHurtSound(DamageSource damageSource) {return TESounds.ROUTINE_HURT.get();}

    @Override
    protected SoundEvent getDeathSound() {
        return TESounds.ROUTINE_DEATH.get();
    }
    @Override
    public boolean isNoGravity(){ return true; }

    // 转换阶段
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(stage == 1) return false;
        return super.hurt(pSource, pAmount);
    }

}
