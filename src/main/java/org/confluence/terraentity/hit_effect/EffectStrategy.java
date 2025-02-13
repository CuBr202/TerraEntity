package org.confluence.terraentity.hit_effect;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;
import org.confluence.terraentity.registries.EffectStrategies;
import org.confluence.terraentity.utils.TEUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <h1>攻击时给敌人施加的效果或回调</h1>
 * <p> 适用于剑、弹射物、生物命中特效等 </p>
 * @author coffee
 */
public class EffectStrategy {
    String name;
    BiConsumer<LivingEntity, LivingEntity> effect;
    public EffectStrategy(String name, BiConsumer<LivingEntity, LivingEntity> effect) {
        this.name = name;
        this.effect = effect;
    }
    public BiConsumer<LivingEntity, LivingEntity> getEffect() {
        return effect;
    }
    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return "effect.strategy." + EffectStrategies.EFFECT_STRATEGY_REGISTRY.getKey(this);
    }
    public MutableComponent getDescription() {
        return Component.translatable(getTranslationKey());
    }

    /**
     * 效果描述
     */
    public static void appendDescription(List<Component> tooltipComponents, List<EffectStrategy> effectStrategy, Component title) {
        int size = effectStrategy.size();
        if(size == 0) return;
        tooltipComponents.add(title);
        for(int i = 0; i < size; i++) {
            EffectStrategy effect = effectStrategy.get(i);
            tooltipComponents.add(Component.literal(" - ").append(effect.getDescription()).withColor(0xFF00FF));
        }
    }

    // 只用于data gen
    String description_en_us;
    String description_zh_cn;
    public EffectStrategy(String name, String description_en_us, String description_zh_cn, BiConsumer<LivingEntity, LivingEntity> effect) {
        this.name = name;
        this.description_en_us = description_en_us;
        this.description_zh_cn = description_zh_cn;
        this.effect = effect;
    }
    public String getDescription_en_us() {
        return description_en_us == null? name : description_en_us;
    }
    public String getDescription_zh_cn() {
        return description_zh_cn == null? name : description_zh_cn;
    }


    /**BASE: 最广泛的接口，可以派生出其他效果
     * <p>effect 效果</p>
     * <p>ticks 持续时间</p>
     * <p>minAmplifier 最小强度</p>
     * <p>maxAmplifier 最大强度</p>
     * <p>possibility 概率</p>
     * */
    public static final FifFunction<Holder<MobEffect>,Integer,Integer,Integer,Float, BiConsumer<LivingEntity,LivingEntity>> TIME_POSSIBILITY_AMPLIFIER_EFFECT = (effect, ticks, minAmplifier, maxAmplifier, possibility)->
            (owner, entity) ->{
                if(entity.getRandom().nextFloat() < possibility) {
                    if (entity.hasEffect(effect)) {
                        MobEffectInstance effect1 = entity.getEffect(effect);
                        if (effect1!=null&& effect1.getAmplifier() < maxAmplifier) {
                            entity.addEffect(new MobEffectInstance(effect, ticks, effect1.getAmplifier() + 1, false, true, false));
                        } else {
                            entity.addEffect(new MobEffectInstance(effect, ticks, maxAmplifier, false, true, false));
                        }
                    } else {
                        entity.addEffect(new MobEffectInstance(effect, ticks, minAmplifier, false, true, false));
                    }
                }};

    /**概率附加ticks的效果*/
    public static final TriFunction<Holder<MobEffect>,Integer,Float, BiConsumer<LivingEntity,LivingEntity>> TIME_POSSIBILITY_EFFECT = (effect, ticks, possibility)->
            TIME_POSSIBILITY_AMPLIFIER_EFFECT.apply(effect, ticks, 0,0, possibility);


    /**附加ticks的效果*/
    public static final BiFunction<Holder<MobEffect>,Integer, BiConsumer<LivingEntity,LivingEntity>> TIME_EFFECT = (effect, ticks)->
            TIME_POSSIBILITY_EFFECT.apply(effect, ticks, 1f);

    /**概率附加随机效果*/
    public static final Function<Map<DeferredHolder<EffectStrategy,EffectStrategy>, Float>, DeferredHolder<EffectStrategy,EffectStrategy>> RANDOM_POSSIBILITY_EFFECT = (consumer_map)->
            TEUtils.getRandomByWeight(consumer_map);


    /* *****************************************************************************************************************************************/

    /**占位符，未定义效果，暂时用发光代替*/
    public static final BiConsumer<LivingEntity,LivingEntity> UNDEFINED_EFFECT =
            TIME_EFFECT.apply(MobEffects.GLOWING, 20*5);

    /**命中时残留弹幕*/
    public static final Function<Function<Level, Projectile>, BiConsumer<LivingEntity, LivingEntity>>  ON_HIT_PROJECTILE = (supplier)-> (owner, entity)->{
        Projectile projectile = supplier.apply(owner.level());
        projectile.setOwner(owner);
        projectile.setPos(entity.position().add(entity.getRandom().nextFloat()*0.2f, entity.getEyeHeight()*0.5f, entity.getRandom().nextFloat()*0.2f));
        owner.level().addFreshEntity(projectile);
    };

    /*其他回调用法********************************************************************************************************************************/
    /**着火*/
    public static final BiFunction<Integer,Float,BiConsumer<LivingEntity,LivingEntity>> SET_FIRE = (ticks,possibility) ->
            (owner1,entity1)->{ if(entity1.getRandom().nextFloat() < possibility)  entity1.setRemainingFireTicks(ticks);};


    /* ***********************************************************************************************************************************************/

    @FunctionalInterface
    public interface QuaFunction<A,B,C,D,R>{
        R apply(A a,B b,C c,D d);
    }
    @FunctionalInterface
    public interface FifFunction<A,B,C,D,E,R>{
        R apply(A a,B b,C c,D d,E e);
    }
}
