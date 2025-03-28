package org.confluence.terraentity.init.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.component.SingleBooleanComponent;
import org.confluence.terraentity.init.TEDataComponentTypes;
import org.confluence.terraentity.init.TEEffectStrategies;
import org.confluence.terraentity.item.Boomerang;


public class TEBoomerangItems {

    private static final TriFunction<Integer,Float,Float,Boomerang.BoomerangModifier> NORMAL_BOOMERANG_MODIFIER =
            (forwardTick, flySpeedFactor, backSpeedFactor) ->   new Boomerang.BoomerangModifier().setForwardTick(forwardTick).setFlySpeedFactor(flySpeedFactor).setBackSpeedFactor(backSpeedFactor);
    private static final TriFunction<Integer,Integer,Boomerang.BoomerangModifier,Boomerang.BoomerangModifier> MULTI_BOOMERANG_MODIFIER =
            (cd, count, modifier) ->    modifier.setNotWaitForBack().setCd(cd).setMaxCount(count);


    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraEntity.MODID);


    public static final DeferredItem<Boomerang> WOOD_BOOMERANG = register("wood_boomerang",3,
            new Boomerang.BoomerangModifier()
    );
    public static final DeferredItem<Boomerang> ENCHANTED_BOOMERANG = register("enchanted_boomerang",4.4f,
            NORMAL_BOOMERANG_MODIFIER.apply(15,1.55f,1.55f)

    );
    public static final DeferredItem<Boomerang> SHROOMERANG = register("shroomerang",4.4f,
            NORMAL_BOOMERANG_MODIFIER.apply(15,1.55f,1.55f)
    );
    public static final DeferredItem<Boomerang> ICE_BOOMERANG = register("ice_boomerang",5.5f,
            NORMAL_BOOMERANG_MODIFIER.apply(16,1.6f,1.6f)
                    .setOnHitEffect(TEEffectStrategies.Components.FROST_BURN_BOOMERANG_EFFECT.get())
    );   //50% 的几率造成 3 秒的霜冻。

    public static final DeferredItem<Boomerang> TRIMARANG = register("trimarang",5.5f,
            MULTI_BOOMERANG_MODIFIER.apply(10,3,
                    NORMAL_BOOMERANG_MODIFIER.apply(17,1.85f,1.85f))
    );   // 使用三次

    public static final DeferredItem<Boomerang> FLAMARANG = register("flamarang",12.5f,
            NORMAL_BOOMERANG_MODIFIER.apply(18,1.85f,1.85f)
                    .setOnHitEffect(TEEffectStrategies.Components.HELL_FIRE_EFFECT.get())
    );   //狱炎效果。


    public static final DeferredItem<Boomerang> DEVELOPER_BOOMERANG = register("developer_boomerang",20,
            new Boomerang.BoomerangModifier().setNotWaitForBack().setCd(10) // 不需要等待返回，设置cd
                    .setForwardTick(50)
                    .setFlySpeedFactor(1.5f) // 设置向前飞行速度
                    .setBackSpeedFactor(2f) // 设置后退速度
    );


    public static final DeferredItem<Boomerang> BeiDou_BOOMERANG = register("bei_dou_boomerang",10,
            MULTI_BOOMERANG_MODIFIER.apply(5, 4,
                    NORMAL_BOOMERANG_MODIFIER.apply(40, 3.0f, 3.0f)
                            .setMaxPenetration(7)
                            .setOnHitEffect(TEEffectStrategies.Components.BEI_DOU_EFFECT.get())
            )
    );

    private static DeferredItem<Boomerang> register(String name, float damage, Boomerang.BoomerangModifier boomerangModifier) {
        return ITEMS.register(name, () -> new Boomerang(damage,boomerangModifier,new Item.Properties().stacksTo(1)
                .component(TEDataComponentTypes.BOOMERANG_READY, SingleBooleanComponent.TRUE)
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                .component(DataComponents.ATTRIBUTE_MODIFIERS, boomerangModifier.attributeModifiersBuilder.build())));
    }

    public static void acceptTag(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tag) {
        ITEMS.getEntries().forEach(item -> tag.add(item.get()));
    }
}
