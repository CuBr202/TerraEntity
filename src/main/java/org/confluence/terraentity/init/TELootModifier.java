package org.confluence.terraentity.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.util.AdditionLootModifier;

public final class TELootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TerraEntity.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADDITION = GLOBAL_LOOT_MODIFIER_SERIALIZER.register("addition", AdditionLootModifier.CODEC);
}