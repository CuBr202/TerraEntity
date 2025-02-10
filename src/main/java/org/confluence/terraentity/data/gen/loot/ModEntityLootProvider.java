package org.confluence.terraentity.data.gen.loot;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;


import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider {
    public ModEntityLootProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(TEEntities.BLOODY_SPORE.get(),ZOMBIE_COMMON_LOOT_TABLE.apply(LootTable.lootTable()));


    }
    private final BiFunction<Item, Float, LootPool.Builder> LOOT_POOL = (item, chance)->
            LootPool.lootPool()
                    .setRolls(BinomialDistributionGenerator.binomial(1, chance))
                    .add(LootItem.lootTableItem(item)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1F)))
                            .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(0.0F, 1.0F))))
            ;

    private final PropertyDispatch.TriFunction<Item ,Float,Float, LootPool.Builder> LOOT_POOL_CONDITIONAL = (item, chance, condition)->
            LOOT_POOL.apply(item, chance).when(LootItemRandomChanceCondition.randomChance(condition));

    private final Function<LootTable.Builder, LootTable.Builder> ZOMBIE_COMMON_LOOT_TABLE = (loot)-> loot
            .withPool(LOOT_POOL.apply(TEItems.BLACK_SLIME_SPAWN_EGG.get(), 0.75F))
            .withPool(LOOT_POOL_CONDITIONAL.apply(TEItems.EYE_OF_CTHULHU_SPAWN_EGG.get(), 0.5F, 0.5F))

            ;


    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return TEEntities.ENTITIES.getEntries().stream().map(RegistryObject::get);
    }
}
