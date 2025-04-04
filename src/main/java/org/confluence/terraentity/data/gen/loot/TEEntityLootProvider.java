package org.confluence.terraentity.data.gen.loot;


import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;
import org.confluence.terraentity.data.enchantment.TEEnchantments;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.item.TERiddenItems;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;


import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class TEEntityLootProvider extends EntityLootSubProvider {
    public TEEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {

        var enchantbuilder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantbuilder.set(this.registries.lookup(Registries.ENCHANTMENT).get().get(TEEnchantments.MULTI_BOOMERANG).get(),1);

        //
        this.add(TEMonsterEntities.NYMPH.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(BinomialDistributionGenerator.binomial(1, 0.5f))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1)))
                        .apply(SetComponentsFunction.setComponent(DataComponents.ENCHANTMENTS,enchantbuilder.toImmutable()))
                )));

        var enchantbuilder1 = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantbuilder1.set(this.registries.lookup(Registries.ENCHANTMENT).get().get(TEEnchantments.WHIP_SWEEP).get(),1);

        this.add(TEMonsterEntities.SNATCHER.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(BinomialDistributionGenerator.binomial(1, 0.1f))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1)))
                        .apply(SetComponentsFunction.setComponent(DataComponents.ENCHANTMENTS,enchantbuilder1.toImmutable()))
                )));

        this.add(TEMonsterEntities.MAN_EATER.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(BinomialDistributionGenerator.binomial(1, 0.1f))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1)))
                        .apply(SetComponentsFunction.setComponent(DataComponents.ENCHANTMENTS,enchantbuilder1.toImmutable()))
                )));

        // 史王
        Stream.of(
                TEMonsterEntities.BLUE_SLIME,
                TEMonsterEntities.GREEN_SLIME,
                TEMonsterEntities.PINK_SLIME,
                TEMonsterEntities.CORRUPTED_SLIME,
                TEMonsterEntities.DESERT_SLIME,
                TEMonsterEntities.JUNGLE_SLIME,
                TEMonsterEntities.EVIL_SLIME,
                TEMonsterEntities.ICE_SLIME,
                TEMonsterEntities.LAVA_SLIME,
                TEMonsterEntities.LUMINOUS_SLIME,
                TEMonsterEntities.CRIMSON_SLIME,
                TEMonsterEntities.PURPLE_SLIME,
                TEMonsterEntities.RED_SLIME,
                TEMonsterEntities.TROPIC_SLIME,
                TEMonsterEntities.YELLOW_SLIME,
                TEMonsterEntities.HONEY_SLIME,
                TEMonsterEntities.BLACK_SLIME,
                TEMonsterEntities.SWAMP_SLIME,
                TEMonsterEntities.GREEN_DUMPLING_SLIME).forEach(e->{
            this.add(e.get(), LootTable.lootTable()
                    .withPool(LOOT_POOL.apply(TESpawnEggItems.KING_SLIME_SPAWN_EGG, 0.01F))
                    .withPool(LOOT_POOL.apply(Items.SLIME_BALL, 0.2F))
                    .withPool(LOOT_POOL.apply(TESummonItems.SLIME_STAFF, 0.001F))
            );
        });

        this.add(TEBossEntities.KING_SLIME.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESummonItems.SLIME_STAFF, 0.33F))
                .withPool(LOOT_POOL.apply(TEWhipItems.SWAMP_WHIP, 0.33F))
                .withPool(LOOT_POOL.apply(TERiddenItems.SLIMY_SADDLE, 0.2F))
        );



        // 克眼
        this.add(TEMonsterEntities.DEMON_EYE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.EYE_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.5F))
                .withPool(LOOT_POOL.apply(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG, 0.5F))
                .withPool(LOOT_POOL.apply(TESummonItems.SLIME_STAFF, 1F))
        );


        // 克脑
        this.add(TEMonsterEntities.BLOODY_SPORE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.2F))
        );
        this.add(TEMonsterEntities.BLOOD_CRAWLER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.DRIPPLER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.BLOOD_ZOMBIE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.BRAIN_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESummonItems.IRON_GOLEM_STAFF, 1F))
        );

        // 世吞
        this.add(TEMonsterEntities.EATER_OF_SOULS.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.DEVOURER.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.DEVOURER_SPAWN_EGG, 0.1F))
        );

        this.add(TEBossEntities.EATER_OF_WORLDS.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESummonItems.IRON_GOLEM_STAFF, 1F))
        );

        // 蜂王
        this.add(TEMonsterEntities.HORNET.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(TESpawnEggItems.QUEEN_BEE_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.QUEEN_BEE.get(), LootTable.lootTable()
                .withPool(LOOT_POOL.apply(Items.BEE_SPAWN_EGG, 1f))
                .withPool(LOOT_POOL.apply(TERiddenItems.HONEYED_GOGGLES, 0.2f))
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
            .withPool(LOOT_POOL.apply(TESpawnEggItems.BLACK_SLIME_SPAWN_EGG, 0.75F))
            .withPool(LOOT_POOL_CONDITIONAL.apply(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG, 0.5F, 0.5F))

            ;


    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                //
                TEMonsterEntities.NYMPH,
                TEMonsterEntities.SNATCHER,
                TEMonsterEntities.MAN_EATER,


                // 史王
                TEMonsterEntities.BLUE_SLIME,
                TEMonsterEntities.GREEN_SLIME,
                TEMonsterEntities.PINK_SLIME,
                TEMonsterEntities.CORRUPTED_SLIME,
                TEMonsterEntities.DESERT_SLIME,
                TEMonsterEntities.JUNGLE_SLIME,
                TEMonsterEntities.EVIL_SLIME,
                TEMonsterEntities.ICE_SLIME,
                TEMonsterEntities.LAVA_SLIME,
                TEMonsterEntities.LUMINOUS_SLIME,
                TEMonsterEntities.CRIMSON_SLIME,
                TEMonsterEntities.PURPLE_SLIME,
                TEMonsterEntities.RED_SLIME,
                TEMonsterEntities.TROPIC_SLIME,
                TEMonsterEntities.YELLOW_SLIME,
                TEMonsterEntities.HONEY_SLIME,
                TEMonsterEntities.BLACK_SLIME,
                TEMonsterEntities.SWAMP_SLIME,
                TEMonsterEntities.GREEN_DUMPLING_SLIME,

                TEBossEntities.KING_SLIME,

                // 克眼
                TEMonsterEntities.DEMON_EYE,

                TEBossEntities.EYE_OF_CTHULHU,

                // 克脑
                TEMonsterEntities.BLOODY_SPORE,
                TEMonsterEntities.BLOOD_CRAWLER,
                TEMonsterEntities.DRIPPLER,
                TEMonsterEntities.BLOOD_ZOMBIE,

                TEBossEntities.BRAIN_OF_CTHULHU,


                // 世吞
                TEMonsterEntities.EATER_OF_SOULS,
                TEMonsterEntities.DEVOURER,

                TEBossEntities.EATER_OF_WORLDS,

                // 蜂王
                TEMonsterEntities.HORNET,
                TEBossEntities.QUEEN_BEE


        ).map(DeferredHolder::get);
    }
}
