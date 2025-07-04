package org.confluence.terraentity.data.gen.loot;


import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terraentity.init.TEEntities;
import org.confluence.terraentity.init.entity.TEBossEntities;
import org.confluence.terraentity.init.entity.TEMonsterEntities;
import org.confluence.terraentity.init.item.TERideableItems;
import org.confluence.terraentity.init.item.TESpawnEggItems;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;


import java.util.ArrayList;
import java.util.stream.Stream;

public class TEEntityLootProvider extends EntityLootSubProvider {
    public TEEntityLootProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {

        // 史王
        Stream.of(TEMonsterEntities.BLUE_SLIME, TEMonsterEntities.GREEN_SLIME, TEMonsterEntities.PINK_SLIME, TEMonsterEntities.CORRUPT_SLIME, TEMonsterEntities.DESERT_SLIME, TEMonsterEntities.JUNGLE_SLIME, TEMonsterEntities.EVIL_SLIME, TEMonsterEntities.ICE_SLIME, TEMonsterEntities.LAVA_SLIME, TEMonsterEntities.LUMINOUS_SLIME, TEMonsterEntities.CRIMSLIME, TEMonsterEntities.PURPLE_SLIME, TEMonsterEntities.RED_SLIME, TEMonsterEntities.TROPIC_SLIME, TEMonsterEntities.YELLOW_SLIME, TEMonsterEntities.HONEY_SLIME, TEMonsterEntities.BLACK_SLIME, TEMonsterEntities.SWAMP_SLIME, TEMonsterEntities.GREEN_DUMPLING_SLIME
                ).forEach(e->{
            this.add(e.get(), LootTable.lootTable()
                    .withPool(singleItemPool(TESpawnEggItems.KING_SLIME_SPAWN_EGG, 0.01F))
                    .withPool(singleItemPool(Items.SLIME_BALL, 0.2F))
                    .withPool(singleItemPool(TESummonItems.SLIME_STAFF, 0.001F))
            );
        });

        this.add(TEBossEntities.KING_SLIME.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESummonItems.SLIME_STAFF, 0.33F))
                .withPool(singleItemPool(TEWhipItems.SWAMP_WHIP, 0.33F))
                .withPool(singleItemPool(TERideableItems.SLIMY_SADDLE, 0.2F))
        );



        // 克眼
        this.add(TEMonsterEntities.DEMON_EYE.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.EYE_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.EYE_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.5F))
                .withPool(singleItemPool(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG, 0.5F))
                .withPool(singleItemPool(TESummonItems.SLIME_STAFF, 1F))
        );


        // 克脑
        this.add(TEMonsterEntities.BLOODY_SPORE.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.2F))
        );
        this.add(TEMonsterEntities.BLOOD_CRAWLER.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.DRIPPLER.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.BLOOD_ZOMBIE.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.BRAIN_OF_CTHULHU_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.BRAIN_OF_CTHULHU.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESummonItems.IRON_GOLEM_STAFF, 1F))
        );

        // 世吞
        this.add(TEMonsterEntities.EATER_OF_SOULS.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.EATER_OF_WORLD_SPAWN_EGG, 0.05F))
        );
        this.add(TEMonsterEntities.DEVOURER.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.DEVOURER_SPAWN_EGG, 0.1F))
        );

        this.add(TEBossEntities.EATER_OF_WORLDS.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESummonItems.IRON_GOLEM_STAFF))
        );

        // 蜂王
        this.add(TEMonsterEntities.HORNET.get(), LootTable.lootTable()
                .withPool(singleItemPool(TESpawnEggItems.QUEEN_BEE_SPAWN_EGG, 0.05F))
        );

        this.add(TEBossEntities.QUEEN_BEE.get(), LootTable.lootTable()
                .withPool(singleItemPool(Items.BEE_SPAWN_EGG, 1,1f))
                .withPool(singleItemPool(TERideableItems.HONEYED_GOGGLES.get(),1, 0.2f))
        );
    }


    public static LootPool.Builder singleItemPool(ItemLike item, int count, float chance){
        return weightLootPool(singleItem(item, count), chance);
    }

    public static LootPool.Builder singleItemPool(ItemLike item, float chance){
        return weightLootPool(singleItem(item, 1), chance);
    }

    public static LootPool.Builder singleItemPool(ItemLike item){
        return weightLootPool(singleItem(item, 1), 1);
    }

    public static LootPool.Builder singleItemPool(RegistryObject<? extends Item> item, int count, float chance){
        return singleItemPool(item.get(), count, chance);
    }

    public static LootPool.Builder singleItemPool(RegistryObject<? extends Item> item, float chance){
        return singleItemPool(item.get(), chance);
    }

    public static LootPool.Builder singleItemPool(RegistryObject<? extends Item> item){
        return singleItemPool(item.get());
    }

    public static LootPool.Builder weightLootPool(LootPoolSingletonContainer.Builder<?> builder, float chance){
        if(chance >= 1){
            return LootPool.lootPool().add(builder);
        }
        int weight = (int) (chance * 1000);
        int emptyWeight = 1000 - weight;
        return LootPool.lootPool().add(builder.setWeight(weight)).add(EmptyLootItem.emptyItem().setWeight(emptyWeight));
    }


    public static LootPoolSingletonContainer.Builder<?> singleItem(ItemLike item, int count){
        if(count == 1)
            return LootItem.lootTableItem(item);
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    public static LootPoolSingletonContainer.Builder<?> singleItem(ItemLike item, int countMin, int countMax){
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(countMin, countMax)));
    }

    private LootPoolSingletonContainer.Builder<?> singleItem(ItemLike item, int count, float enchantmentChance){
        return singleItem(item, count).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(0.0F, Math.max(count * enchantmentChance, 1.0F))));
    }

    private LootPoolSingletonContainer.Builder<?> singleItem(ItemLike item, int countMin, int countMax, float enchantmentChance){
        return singleItem(item, countMin, countMax).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(0.0F, Math.max(countMax * enchantmentChance, 1.0F))));
    }



    private Stream<EntityType<?>> getIterableFromRegister(DeferredRegister<EntityType<?>> register) {
        return new ArrayList<EntityType<?>>(
                register.getEntries().stream()
                        .map(RegistryObject::get)
                        .filter(map::containsKey)
                        .toList()
        ).stream();
    }

//    protected void add(EntityType<?> entityType, LootTable.Builder builder) {
//        ResourceKey<LootTable> resourceKey = ResourceKey.create(Registries.LOOT_TABLE, BuiltInRegistries.ENTITY_TYPE.getKey(entityType).withPrefix("te"));
//        this.add(entityType, resourceKey, builder);
//    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return getIterableFromRegister(TEEntities.ENTITIES);
    }
}
