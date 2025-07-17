package org.confluence.terraentity.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TESummonItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record TESubLoot(HolderLookup.Provider registries) implements LootTableSubProvider {



    public static final ResourceKey<LootTable> SPAWN_WOODEN_SWORD_STAFF = getLootTableKey("chest/spawn_wooden_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_STONE_SWORD_STAFF = getLootTableKey("chest/spawn_stone_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_IRON_SWORD_STAFF = getLootTableKey("chest/spawn_iron_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_GOLDEN_SWORD_STAFF = getLootTableKey("gameplay/spawn_golden_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_DIAMOND_SWORD_STAFF = getLootTableKey("entities/spawn_diamond_sword_staff");
    public static final ResourceKey<LootTable> SPAWN_NETHERITE_SWORD_STAFF = getLootTableKey("chest/spawn_netherite_sword_staff");

    public static final ResourceKey<LootTable> SPAWN_SCULK_WISP_STAFF = getLootTableKey("chest/spawn_sculk_wisp_staff");

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {

        consumer.accept(SPAWN_WOODEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_WOODEN_SWORD_STAFF.get(), 0.15f))
        );
        //
        consumer.accept(SPAWN_STONE_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_STONE_SWORD_STAFF.get(), 0.15f))
        );
        consumer.accept(SPAWN_IRON_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_IRON_SWORD_STAFF.get(), 0.15f))
        );
        consumer.accept(SPAWN_GOLDEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_GOLDEN_SWORD_STAFF.get(), 0.05f))
        );
        consumer.accept(SPAWN_DIAMOND_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_DIAMOND_SWORD_STAFF.get(), 0.05f))
        );
        consumer.accept(SPAWN_NETHERITE_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_NETHERITE_SWORD_STAFF.get(), 0.15f))
        );

        consumer.accept(SPAWN_SCULK_WISP_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SCULK_WISP_STAFF.get(), 0.3f))
        );
    }

    static ResourceKey<LootTable> getLootTableKey(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, TerraEntity.space("modifier/" +name));
    }
}