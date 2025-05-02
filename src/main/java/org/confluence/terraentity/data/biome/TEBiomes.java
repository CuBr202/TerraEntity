package org.confluence.terraentity.data.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.data.gen.biome.ExtendedAddSpawnsBiomeModifier;
import org.confluence.terraentity.data.gen.biome.NPCAddSpawnsBiomeModifier;


public class TEBiomes {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, TerraEntity.MODID);

    public static final RegistryObject<Codec<ExtendedAddSpawnsBiomeModifier>> ADD_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("extended_add_spawns", () -> ExtendedAddSpawnsBiomeModifier.CODEC);
    public static final RegistryObject<Codec<NPCAddSpawnsBiomeModifier>> NPC_SPAWNS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("npc_add_spawns", () -> NPCAddSpawnsBiomeModifier.CODEC);

    public static void register(IEventBus bus) {
        BIOME_MODIFIER_SERIALIZERS.register(bus);
    }

    static void createBiomes(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER);


    }
}
