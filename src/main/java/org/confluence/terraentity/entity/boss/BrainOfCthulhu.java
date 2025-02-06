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
import org.confluence.terraentity.entity.monster.VisualNeuron;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TESounds;
import org.confluence.terraentity.utils.TEUtils;
import org.joml.Quaternionf;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BrainOfCthulhu extends AbstractTerraBossBase implements GeoEntity, Boss {
    private static final float MAX_HEALTHS = 552f;
    private static final float DAMAGE = 14f;//接触伤害
    private static final float MOVE_SPEED = 0.3f;
    private int minionsCount = 20; // 随从数量
    private int minionsSummonInternal = 10; // 随从攻击间隔
    private int dashCount = 2; // 冲刺次数, 血量低于30%时冲刺3次

    private float _dashCount = dashCount;
    private float _moveSpeed = MOVE_SPEED;
    private final List<VisualNeuron> minions = new LinkedList<>(); // 随从实体
    private final List<Vec3> homePoses = new ArrayList<>(); // 随从初始位置

    public int stage = 1; //阶段
    private Vec3 inertia;
    private Curve curve;

    public BrainOfCthulhu(EntityType<? extends BrainOfCthulhu> entityType, Level level) {
        super(entityType, level, MAX_HEALTHS, 3);
        //初始属性
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE);
//        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.playSound(TESounds.ROAR.get());
        this.noPhysics = true;
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
                        VisualNeuron minion = TEEntities.VISUAL_NEURON.get().create(level());
                        minions.add(minion);

                        float r = random.nextFloat() + 5;
                        float theta = random.nextFloat()  * (float) Math.PI;
                        float beta = random.nextFloat() * (float) Math.PI ;
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

                    if(skills.tick % minionsSummonInternal == 0) {
                        boolean exist = false;
                        for (VisualNeuron m : minions) {
                            if (m.isReady()) {
                                m.attack(getTarget());
                                exist = true;
                                break;
                            }
                        }
                        if(!exist && skills.tick > 60){{
                            skills.forceEnd();
                        }}
                    }
                    LookAt(10);

                    // 向玩家斜上方移动
                    Vec3 dir = position().subtract(target.position()).normalize().multiply(1,0,1);
                    Vec3 tar = target.position().add(0,5,0).add(dir.scale(10));
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
                    Vec3 tar = getTarget().position().add(0,1,0);
                    if (distanceToSqr(tar) > 2)
                        setDeltaMovement(tar.subtract(position()).normalize().scale(_moveSpeed / 2));
                })
                .onOver(e->{
                    skills.forceStartIndex(1);
                })
        ;

        switch_1_to_2 = new BossSkill<BrainOfCthulhu>(switching, 15, 0)
                .onTick(e->{
                    LookAt(10);
                })
                .onOver(e->{
                    _moveSpeed = 0.5f;
                    noPhysics = true;
                    for(int i=1;i<4;i++){
                        BrainFake fake = TEEntities.BRAIN_FAKE.get().create(level());
                        fake.setPos(position());
                        fake.setOwner(this);
                        fake.tag = i;
                        level().addFreshEntity(fake);
                    }
                })
        ;

        stage2_stare = new BossSkill<BrainOfCthulhu>(open, 40, 0)
                .onInit(e->{
                    if(target != null){
                        float r = random.nextFloat() + 16;
                        float theta = random.nextFloat() * 2 * (float) Math.PI;
                        Vec3 control = target.position().add(r * Math.sin(theta), 2, r * Math.cos(theta));
                        Vec3 end = control.add(0,3,0);
                        curve = new Bezier3Curse(position(), control, end);
                    }
                })
                .onTick(e->{
                    if(target == null || curve == null) return;
                    setPos(curve.cal(skills.tick / 40f));
                    LookAt(10);
                })
        ;

        state2_dash = new BossSkill<BrainOfCthulhu>(open, 30, 10)
                .onInit(e->{

                })
                .onTick(e->{
                    if(target == null) return;
                    if(!skills.canContinue()){
                        setDeltaMovement(position().subtract(target.position()).normalize().scale(0.3f));
                    }
                    if(skills.canTrigger()){
                        Vec3 control = target.position().add(random.nextFloat() - 0.5f, -2, random.nextFloat() - 0.5f);
                        Vec3 end = target.position().add(target.position().subtract(position()).normalize().multiply(10,0,10)).add(0,2,0);
                        curve = new Bezier3Curse(position(), control, end);
                        playSound(TESounds.ROAR.get(),5,1);
                    }
                    if(skills.canContinue() && curve != null) {
                        setPos(curve.cal((skills.tick - 10 ) / 20f));
                        LookAt(10);
                    }
                })
                .onOver(e->{
                    if(getHealth() / getMaxHealth() < 0.3f)
                        dashCount = 3;
                    if(_dashCount > 0){
                        _dashCount--;
                        skills.forceStartIndex(5);
                    }else{
                        _dashCount = dashCount;
                    }
                })
        ;

        stage2_fade_in = new BossSkill<BrainOfCthulhu>(open, 30, 0)
                .onTick(e->{
                    if(target != null){
                        Vec3 dir = position().subtract(target.position()).normalize();
                        setDeltaMovement(dir.scale(0.3f));
                    }
                })
        ;
        stage2_fade_out = new BossSkill<BrainOfCthulhu>(open, 100, 30)
                .onInit(e->{
                    if(getTarget() != null) {
                        float r = random.nextFloat() + 10;
                        float theta = random.nextFloat() * 2 * (float) Math.PI;
                        float beta = (random.nextFloat() * 0.3f + 0.35f) * (float) Math.PI;
                        Vec3 pos = TEUtils.sphere(r, theta, beta);
                        setPos(getTarget().position().add(pos).add(0,2,0));
                    }
                })
                .onTick(e->{
                    if(getTarget() == null) return;
                    LookAt(10);

                    // 向玩家正上方移动
                    Vec3 tar = getTarget().position().add(0,1,0);
                    if (distanceToSqr(tar) > 2 && hurtTime == 0)
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
        if(skills.index == 0){
            return (tickCount -lastSkillTick) / 51f;
        } else if(skills.index == 2)
            return 1 - (tickCount -lastSkillTick) / 41f;
        else if(skills.index == 3 )
            return (tickCount -lastSkillTick) / 41f;
        else if(skills.index == 7)
            return 1 - (tickCount -lastSkillTick) / 31f;
        else if(skills.index == 8)
            return (tickCount -lastSkillTick) / 31f;

        return 1;
    }

    public float getDissolveProgress(){
        return getFadeProgress();
    }

    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !(target instanceof VisualNeuron);
    }

    public void tick() {
        super.tick();
        if(stage == 1 && !level().isClientSide && tickCount > 50){
            int c = 0;
            for(int i = 0; i < minions.size(); i++){
                VisualNeuron m = minions.get(i);
                if(m.isAlive()){
                    Vec3 dir = homePoses.get(i);
                    if(dir != null && random.nextFloat() < 0.5f){
                        // 随机旋转homePos
                        Quaternionf q = new Quaternionf().rotateY((float) (random.nextDouble() * 20) );
                        dir = new Vec3(q.transform(dir.toVector3f()));
                        m.homePos = position().add(dir);
                    }else{
                        m.homePos = position().add(homePoses.get(i));
                    }
                    c++;
                }
            }
            minionsCount = c;
        }
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5f);
        this.skills.forceStartIndex(0);
    }

    @Override
    public float getBossEventProgress(){
        float hp = getHealth();
        float maxHp = getMaxHealth();
        for(VisualNeuron m : minions){
            hp += m.getHealth();
            maxHp += m.getMaxHealth();
        }
        return hp / maxHp;
    }

    @Override
    public int getDetectInternal() {
        return 1;
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

    @Override
    public boolean isPickable() {
        return this.stage != 1;
    }

}
