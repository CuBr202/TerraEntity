package org.confluence.terraentity.init.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terraentity.data.component.EffectStrategyComponent;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEParticles;
import org.confluence.terraentity.item.BaseWhipItem;
import org.confluence.terraentity.registries.hit_effect.variant.TimePossibilityAmplifierEffect;

import java.util.function.Function;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEWhipItems {
    //    public static final DeferredRegister.Items SENTRY_ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Whip Items
    public static final DeferredItem<BaseWhipItem> LEATHER_WHIP = registerWhip("leather_whip", 9f, 2, 0.2F, 20,0, p->p
            .setDurability(200)
            .component(TEDataComponentTypes.EFFECT_STRATEGY_BENEFICIAL, EffectStrategyComponent.of(
                    TimePossibilityAmplifierEffect.of("strength", MobEffects.DAMAGE_BOOST, 100)
            )));
    public static final DeferredItem<BaseWhipItem> SLUB_WHIP = registerWhip("slub_whip", 5f, 3, 0, 20,0.1F, p->p
            .setDurability(300)
            .setBlock(Blocks.BAMBOO::defaultBlockState)
    );
    public static final DeferredItem<BaseWhipItem> RUBY_WHIP = registerWhip("ruby_whip", 7.8f, 2, 0.1f, 20,0, p->p
            .setDurability(760)
    );
    public static final DeferredItem<BaseWhipItem> AMBER_WHIP = registerWhip("amber_whip", 7.7f, 2, 0.1f, 20,0, p->p
            .setDurability(740)
    );
    public static final DeferredItem<BaseWhipItem> TOPAZ_WHIP = registerWhip("topaz_whip", 7.5f, 2, 0.1f, 20,0, p->p
            .setDurability(700)
    );
    public static final DeferredItem<BaseWhipItem> EMERALD_WHIP = registerWhip("emerald_whip",7.6f, 2, 0.1f, 20,0, p->p
            .setDurability(900)
    );
    public static final DeferredItem<BaseWhipItem> DIAMOND_WHIP = registerWhip("diamond_whip", 7.7f, 2, 0.1f, 20,0, p->p
            .setDurability(1000)
    );
    public static final DeferredItem<BaseWhipItem> SAPPHIRE_WHIP = registerWhip("sapphire_whip", 7.6f, 2, 0.1f, 20,0, p->p
            .setDurability(720)
    );
    public static final DeferredItem<BaseWhipItem> AMETHYST_WHIP = registerWhip("amethyst_whip", 7.5f, 2, 0.1f, 20,0, p->p
            .setDurability(700)
    );
    public static final DeferredItem<BaseWhipItem> SWAMP_WHIP = registerWhip("swamp_whip", 9f, 5, 0.5f, 25,0.2f, p->p
            .setDurability(1200)
            .setParticle(TEParticles.LEAVES, 0.01f)
            .component(TEDataComponentTypes.EFFECT_STRATEGY, EffectStrategyComponent.of(
                    new TimePossibilityAmplifierEffect("mud", MobEffects.MOVEMENT_SLOWDOWN, 40,0,0,1)
            )));

    public static DeferredItem<BaseWhipItem> registerWhip(String name,float damage,float markDamage, float attackSpeed,int cooldown,float range, Function<BaseWhipItem.WhipProperties, Item.Properties> whipFactory){
        return ITEMS.register(name, ()->new BaseWhipItem(((BaseWhipItem.WhipProperties)whipFactory.apply(new BaseWhipItem.WhipProperties())).buildProperties(),damage, markDamage, attackSpeed, cooldown, range));
    }
}
