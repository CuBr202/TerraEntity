package org.confluence.terraentity.init.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.api.event.WhipRegisterModifyEvent;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEParticles;
import org.confluence.terraentity.item.BaseWhipItem;
import org.confluence.terraentity.registries.hit_effect.variant.TimePossibilityAmplifierEffect;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.function.Function;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEWhipItems {
    //    public static final DeferredRegister.Items SENTRY_ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Whip Items
    public static final DeferredItem<BaseWhipItem> LEATHER_WHIP = registerWhip("leather_whip", 10f, 2, 0.5f, 15,0.9f, p->p
            .setDurability(200)
            .component(TEDataComponentTypes.EFFECT_STRATEGY_BENEFICIAL, EffectStrategyComponent.of(
                    TimePossibilityAmplifierEffect.of("strength", MobEffects.DAMAGE_BOOST, 100)
            )));
    public static final DeferredItem<BaseWhipItem> SLUB_WHIP = registerWhip("slub_whip", 8f, 3, 0.2f, 15,0.5f, p->p
            .setDurability(300)
            .setBlock(Blocks.BAMBOO::defaultBlockState)
    );
    public static final DeferredItem<BaseWhipItem> RUBY_WHIP = registerWhip("ruby_whip", 12.8f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(760)
    );
    public static final DeferredItem<BaseWhipItem> AMBER_WHIP = registerWhip("amber_whip", 12.7f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(740)
    );
    public static final DeferredItem<BaseWhipItem> TOPAZ_WHIP = registerWhip("topaz_whip", 12.5f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(700)
    );
    public static final DeferredItem<BaseWhipItem> JADE_WHIP = registerWhip("jade_whip",12.6f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(900)
    );
    public static final DeferredItem<BaseWhipItem> DIAMOND_WHIP = registerWhip("diamond_whip", 12.7f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(1000)
    );
    public static final DeferredItem<BaseWhipItem> SAPPHIRE_WHIP = registerWhip("sapphire_whip", 12.6f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(720)
    );
    public static final DeferredItem<BaseWhipItem> AMETHYST_WHIP = registerWhip("amethyst_whip", 12.5f, 2, 0.5f, 15,0.8f, p->p
            .setDurability(700)
    );
    public static final DeferredItem<BaseWhipItem> SWAMP_WHIP = registerWhip("swamp_whip", 16f, 5, 1f, 15,1.5f, p->p
            .setDurability(1200)
            .setParticle(TEParticles.LEAVES, 0.01f)
            .component(TEDataComponentTypes.EFFECT_STRATEGY, EffectStrategyComponent.of(
                    new TimePossibilityAmplifierEffect("mud", MobEffects.MOVEMENT_SLOWDOWN, 40,0,0,1)
            )));

    public static DeferredItem<BaseWhipItem> registerWhip(String name,float damage,float markDamage, float attackSpeed,int cooldown,float range, Function<BaseWhipItem.WhipProperties, Item.Properties> whipFactory){

        var event = new WhipRegisterModifyEvent(damage, markDamage, attackSpeed, cooldown, range, name);
        AdapterUtils.postEvent(event);
        if(event.isCanceled()){
            return null;
        }
        return ITEMS.register(event.getName(), ()->new BaseWhipItem(((BaseWhipItem.WhipProperties)whipFactory.apply(new BaseWhipItem.WhipProperties())).buildProperties(),
                event.getDamage() * WhipRegisterModifyEvent.damageFactor, event.getMarkDamage(), event.getAttackSpeed(), event.getCooldown(), event.getRange()));
    }
}
