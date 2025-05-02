package org.confluence.terraentity.data.gen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.terraentity.entity.spawner.NPCSpawner;
import org.jetbrains.annotations.NotNull;

public record NPCAddSpawnsBiomeModifier(HolderSet<Biome> biomes, HolderSet<Biome> excludedBiomes, EntityType<?> entityType, int delay, int weight) implements BiomeModifier {

    public static final Codec<NPCAddSpawnsBiomeModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(NPCAddSpawnsBiomeModifier::biomes),
            Biome.LIST_CODEC.fieldOf("excludedBiomes").forGetter(NPCAddSpawnsBiomeModifier::excludedBiomes),
            ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entityType").forGetter(NPCAddSpawnsBiomeModifier::entityType),
            Codec.INT.fieldOf("delay").forGetter(NPCAddSpawnsBiomeModifier::delay),
            Codec.INT.fieldOf("weight").forGetter(NPCAddSpawnsBiomeModifier::weight)
    ).apply(builder, NPCAddSpawnsBiomeModifier::new));

    @Override
    public void modify(@NotNull Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome) && !this.excludedBiomes.contains(biome)) {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            spawns.addSpawn(MobCategory.AXOLOTLS, new MobSpawnSettings.SpawnerData(this.entityType, weight, 1, 1));
            NPCSpawner.getInstance().delayMap.put(entityType, delay);
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
