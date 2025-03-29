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
    public static final DeferredItem<BaseWhipItem> LEATHER_WHIP = registerWhip("leather_whip", 2f, 2, 0.2F, 20,0, p->p
            .setDurability(100)
    );
        public static final DeferredItem<BaseWhipItem> SLUB_WHIP = registerWhip("slub_whip", 2.3f, 3, 0, 20,0.1F, p->p
            .setDurability(150)
            .setBlock(Blocks.BAMBOO::defaultBlockState)
    );
    public static final DeferredItem<BaseWhipItem> RUBY_WHIP = registerWhip("ruby_whip", 2.8f, 2, 0.1f, 20,0, p->p
            .setDurability(380)
    );
    public static final DeferredItem<BaseWhipItem> AMBER_WHIP = registerWhip("amber_whip", 2.7f, 2, 0.1f, 20,0, p->p
            .setDurability(370)
    );
    public static final DeferredItem<BaseWhipItem> TOPAZ_WHIP = registerWhip("topaz_whip", 2.5f, 2, 0.1f, 20,0, p->p
            .setDurability(350)
    );
    public static final DeferredItem<BaseWhipItem> EMERALD_WHIP = registerWhip("emerald_whip",2.6f, 2, 0.1f, 20,0, p->p
            .setDurability(450)
    );
    public static final DeferredItem<BaseWhipItem> DIAMOND_WHIP = registerWhip("diamond_whip", 2.7f, 2, 0.1f, 20,0, p->p
            .setDurability(500)
    );
    public static final DeferredItem<BaseWhipItem> SAPPHIRE_WHIP = registerWhip("sapphire_whip", 2.6f, 2, 0.1f, 20,0, p->p
            .setDurability(360)
    );
    public static final DeferredItem<BaseWhipItem> AMETHYST_WHIP = registerWhip("amethyst_whip", 2.5f, 2, 0.1f, 20,0, p->p
            .setDurability(350)
    );
    public static final DeferredItem<BaseWhipItem> SWAMP_WHIP = registerWhip("swamp_whip", 4, 5, 0.5f, 25,0.2f, p->p
            .setParticle(TEParticles.LEAVES, 0.01f)
            .component(TEDataComponentTypes.EFFECT_STRATEGY, EffectStrategyComponent.of(
                    new TimePossibilityAmplifierEffect("mud", MobEffects.MOVEMENT_SLOWDOWN, 40,0,0,1)
            )));

    public static DeferredItem<BaseWhipItem> registerWhip(String name,float damage,float markDamage, float attackSpeed,int cooldown,float range, Function<BaseWhipItem.WhipProperties, Item.Properties> whipFactory){
        return ITEMS.register(name, ()->new BaseWhipItem(((BaseWhipItem.WhipProperties)whipFactory.apply(new BaseWhipItem.WhipProperties())).buildProperties(),damage, markDamage, attackSpeed, cooldown, range));
    }
}
