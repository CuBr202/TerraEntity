package org.confluence.terraentity.init.item;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.item.Boomerang;
import org.confluence.terraentity.item.TEItemProperties;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEBoomerangItems {

    private static final TriFunction<Integer,Float,Float,Boomerang.BoomerangModifier> NORMAL_BOOMERANG_MODIFIER =
            (forwardTick, flySpeedFactor, backSpeedFactor) ->   new Boomerang.BoomerangModifier().setForwardTick(forwardTick).setFlySpeedFactor(flySpeedFactor).setBackSpeedFactor(backSpeedFactor);
    private static final TriFunction<Integer,Integer,Boomerang.BoomerangModifier,Boomerang.BoomerangModifier> MULTI_BOOMERANG_MODIFIER =
            (cd, count, modifier) ->    modifier.setNotWaitForBack().setCd(cd).setMaxCount(count);


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MODID);


    public static final RegistryObject<Boomerang> WOOD_BOOMERANG = register("wood_boomerang",3,
            new Boomerang.BoomerangModifier().setDurability(100)
    );
    public static final RegistryObject<Boomerang> ENCHANTED_BOOMERANG = register("enchanted_boomerang",4.4f,
            NORMAL_BOOMERANG_MODIFIER.apply(15,1.55f,1.55f).setDurability(300)

    );
    public static final RegistryObject<Boomerang> SHROOMERANG = register("shroomerang",4.4f,
            NORMAL_BOOMERANG_MODIFIER.apply(15,1.55f,1.55f).setDurability(500)
    );
    public static final RegistryObject<Boomerang> ICE_BOOMERANG = register("ice_boomerang",5.5f,
            NORMAL_BOOMERANG_MODIFIER.apply(16,1.6f,1.6f)
                    .setOnHitEffect(TEEffectStrategies.Components.FROST_BURN_BOOMERANG_EFFECT.get())
                    .setDurability(500)
                    .setParticle(()->ParticleTypes.SNOWFLAKE)
    );   //50% 的几率造成 3 秒的霜冻。

    public static final RegistryObject<Boomerang> TRIMARANG = register("trimarang",5.5f,
            MULTI_BOOMERANG_MODIFIER.apply(10,3,
                    NORMAL_BOOMERANG_MODIFIER.apply(17,1.85f,1.85f)).setDurability(1000)
    );   // 使用三次

    public static final RegistryObject<Boomerang> FLAMARANG = register("flamarang",12.5f,
            NORMAL_BOOMERANG_MODIFIER.apply(18,1.85f,1.85f)
                    .setOnHitEffect(TEEffectStrategies.Components.HELL_FIRE_EFFECT.get())
                    .setDurability(1500)
                    .setParticle(()->ParticleTypes.LAVA)
    );   //狱炎效果。


    public static final RegistryObject<Boomerang> DEVELOPER_BOOMERANG = register("developer_boomerang",20,
            new Boomerang.BoomerangModifier().setNotWaitForBack().setCd(10) // 不需要等待返回，设置cd
                    .setForwardTick(50)
                    .setFlySpeedFactor(1.5f) // 设置向前飞行速度
                    .setBackSpeedFactor(2f) // 设置后退速度
    );


    public static final RegistryObject<Boomerang> BeiDou_BOOMERANG = register("bei_dou_boomerang",10,
            MULTI_BOOMERANG_MODIFIER.apply(5, 4,
                    NORMAL_BOOMERANG_MODIFIER.apply(40, 3.0f, 3.0f)
                            .setMaxPenetration(7)
                            .setOnHitEffect(TEEffectStrategies.Components.BEI_DOU_EFFECT.get())
            )
    );

    private static RegistryObject<Boomerang> register(String name, float damage, Boomerang.BoomerangModifier boomerangModifier) {
        return ITEMS.register(name, () -> new Boomerang(damage,boomerangModifier, (TEItemProperties) new TEItemProperties()
                .component(TEDataComponentTypes.BOOMERANG_READY, SingleBooleanComponent.TRUE)
//                .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
//                .component(DataComponents.ATTRIBUTE_MODIFIERS, boomerangModifier.attributeModifiersBuilder.build())
                .stacksTo(1)
        ));
    }

    public static void acceptTag(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tag) {
        ITEMS.getEntries().forEach(item -> tag.add(item.get()));
    }
}
