package org.confluence.terraentity.data.gen.loot;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.TEItems;


import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class TEEntityLootProvider extends EntityLootSubProvider {
    public TEEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        // 史王
        Stream.of(
                TEEntities.BLUE_SLIME,
                TEEntities.GREEN_SLIME,
                TEEntities.PINK_SLIME,
                TEEntities.CORRUPTED_SLIME,
                TEEntities.DESERT_SLIME,
                TEEntities.JUNGLE_SLIME,
                TEEntities.EVIL_SLIME,
                TEEntities.ICE_SLIME,
                TEEntities.LAVA_SLIME,
                TEEntities.LUMINOUS_SLIME,
                TEEntities.CRIMSON_SLIME,
                TEEntities.PURPLE_SLIME,
                TEEntities.RED_SLIME,
                TEEntities.TROPIC_SLIME,
                TEEntities.YELLOW_SLIME,
                TEEntities.HONEY_SLIME,
                TEEntities.BLACK_SLIME).forEach(e->{
            this.add(e.get(), LootTable.lootTable()
                    .withPool(LOOT_POOL.apply(TEItems.KING_SLIME_SPAWN_EGG, 0.01F))
                    .withPool(LOOT_POOL.apply(Items.SLIME_BALL, 0.2F))
                    .withPool(LOOT_POOL.apply(TEItems.SLIME_STAFF, 0.001F))
            );
        });

        this.add(TEEntities.KING_SLIME.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.SLIME_STAFF, 0.5F))
        );



        // 克眼
        this.add(TEEntities.DEMON_EYE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.EYE_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEEntities.EYE_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.5F))
                .withPool(LOOT_POOL.apply(TEItems.EATER_OF_WORLD_SPAWN_EGG, 0.5F))
                .withPool(LOOT_POOL.apply(TEItems.SLIME_STAFF, 1F))
        );


        // 克脑
        this.add(TEEntities.BLOODY_SPORE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.2F))
        );
        this.add(TEEntities.BLOOD_CRAWLER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEEntities.DRIPPLER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEEntities.BLOOD_ZOMBIE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEEntities.BRAIN_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.IRON_GOLEM_STAFF, 1F))
        );

        // 世吞
        this.add(TEEntities.EATER_OF_SOULS.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.EATER_OF_WORLD_SPAWN_EGG, 0.05F))
        );
        this.add(TEEntities.DEVOURER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.DEVOURER_SPAWN_EGG, 0.1F))
        );

        this.add(TEEntities.EATER_OF_WORLDS.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.IRON_GOLEM_STAFF, 1F))
        );

        // 蜂王
        this.add(TEEntities.HORNET.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TEItems.QUEEN_BEE_SPAWN_EGG, 0.05F))
        );

        this.add(TEEntities.QUEEN_BEE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(Items.BEE_SPAWN_EGG, 1f))
        );
    }

    private final TriFunction<ItemLike,Float,Integer,  LootPool.Builder> COUNT_LOOT_POOL = (item, chance, count)->
            LootPool.lootPool()
                    .setRolls(BinomialDistributionGenerator.binomial(1, chance))
                    .add(LootItem.lootTableItem(item)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, count)))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, count))))
            ;


    private final BiFunction<ItemLike,Float, LootPool.Builder> LOOT_POOL = (item, chance)->COUNT_LOOT_POOL.apply(item, chance, 1);

    private final PropertyDispatch.TriFunction<ItemLike,Float,Float, LootPool.Builder> LOOT_POOL_CONDITIONAL = (item, chance, condition)->
            LOOT_POOL.apply(item, chance).when(LootItemRandomChanceCondition.randomChance(condition));

    private final Function<LootTable.Builder, LootTable.Builder> COMMON_LOOT_TABLE = (loot)-> loot
            .withPool(LOOT_POOL.apply(TEItems.BLACK_SLIME_SPAWN_EGG, 0.75F))
            .withPool(LOOT_POOL_CONDITIONAL.apply(TEItems.EYE_OF_CTHULHU_SPAWN_EGG, 0.5F, 0.5F))

            ;


    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                // 史王
                TEEntities.BLUE_SLIME,
                TEEntities.GREEN_SLIME,
                TEEntities.PINK_SLIME,
                TEEntities.CORRUPTED_SLIME,
                TEEntities.DESERT_SLIME,
                TEEntities.JUNGLE_SLIME,
                TEEntities.EVIL_SLIME,
                TEEntities.ICE_SLIME,
                TEEntities.LAVA_SLIME,
                TEEntities.LUMINOUS_SLIME,
                TEEntities.CRIMSON_SLIME,
                TEEntities.PURPLE_SLIME,
                TEEntities.RED_SLIME,
                TEEntities.TROPIC_SLIME,
                TEEntities.YELLOW_SLIME,
                TEEntities.HONEY_SLIME,
                TEEntities.BLACK_SLIME,

                TEEntities.KING_SLIME,

                // 克眼
                TEEntities.DEMON_EYE,

                TEEntities.EYE_OF_CTHULHU,

                // 克脑
                TEEntities.BLOODY_SPORE,
                TEEntities.BLOOD_CRAWLER,
                TEEntities.DRIPPLER,
                TEEntities.BLOOD_ZOMBIE,

                TEEntities.BRAIN_OF_CTHULHU,


                // 世吞
                TEEntities.EATER_OF_SOULS,
                TEEntities.DEVOURER,

                TEEntities.EATER_OF_WORLDS,

                // 蜂王
                TEEntities.HORNET,
                TEEntities.QUEEN_BEE


        ).map(DeferredHolder::get);
    }
}
