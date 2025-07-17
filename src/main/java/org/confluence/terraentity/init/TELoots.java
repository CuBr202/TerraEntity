package org.confluence.terraentity.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.init.loot.conditioin.VariantCondition;

public class TELoots {


    public static class TELootNumberProviders {

        public static final DeferredRegister<LootNumberProviderType> TYPE = DeferredRegister.create(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.key(), TerraEntity.MODID);

//        public static final RegistryObject<LootNumberProviderType> VARIANT = TYPE.register("variant", () ->
//                new LootNumberProviderType(VariantProvider.CODEC));


    }

    public static class TELootItemConditions {
        public static final DeferredRegister<LootItemConditionType> TYPE = DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE.key(), TerraEntity.MODID);
//        public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> TYPE = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TerraEntity.MODID);

        public static final RegistryObject<LootItemConditionType> VARIANT_CONDITION = TYPE.register("variant_condition", ()->new LootItemConditionType(new VariantCondition.VariantConditionSerializer()));

    }

    public static void register(IEventBus bus) {
//        TELootNumberProviders.TYPE.register(bus);
        TELootItemConditions.TYPE.register(bus);
    }
}
