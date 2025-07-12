package org.confluence.terraentity.data.gen.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.init.item.TESummonItems;

import java.util.function.BiConsumer;

public record TESubLoot() implements LootTableSubProvider {



    public static final ResourceLocation SPAWN_WOODEN_SWORD_STAFF = getLootTableKey("chest/spawn_wooden_sword_staff");
    public static final ResourceLocation SPAWN_STONE_SWORD_STAFF = getLootTableKey("chest/spawn_stone_sword_staff");
    public static final ResourceLocation SPAWN_IRON_SWORD_STAFF = getLootTableKey("chest/spawn_iron_sword_staff");
    public static final ResourceLocation SPAWN_GOLDEN_SWORD_STAFF = getLootTableKey("gameplay/spawn_golden_sword_staff");
    public static final ResourceLocation SPAWN_DIAMOND_SWORD_STAFF = getLootTableKey("entities/spawn_diamond_sword_staff");
    public static final ResourceLocation SPAWN_NETHERITE_SWORD_STAFF = getLootTableKey("chest/spawn_netherite_sword_staff");

    public static final ResourceLocation SPAWN_SCULK_WISP_STAFF = getLootTableKey("chest/spawn_sculk_wisp_staff");

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {

        consumer.accept(SPAWN_WOODEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_WOODEN_SWORD_STAFF.get(), 0.3f))
        );
        //
        consumer.accept(SPAWN_STONE_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_STONE_SWORD_STAFF.get(), 0.3f))
        );
        consumer.accept(SPAWN_IRON_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_IRON_SWORD_STAFF.get(), 0.3f))
        );
        consumer.accept(SPAWN_GOLDEN_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_GOLDEN_SWORD_STAFF.get(), 0.1f))
        );
        consumer.accept(SPAWN_DIAMOND_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_DIAMOND_SWORD_STAFF.get(), 0.05f))
        );
        consumer.accept(SPAWN_NETHERITE_SWORD_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SUMMON_NETHERITE_SWORD_STAFF.get(), 0.3f))
        );

        consumer.accept(SPAWN_SCULK_WISP_STAFF, LootTable.lootTable()
                .withPool(TEEntityLootProvider.singleItemPool(TESummonItems.SCULK_WISP_STAFF.get(), 0.3f))
        );
    }

    static ResourceLocation getLootTableKey(String name) {
        return TerraEntity.space(name);
    }

}