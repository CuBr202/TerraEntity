package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.init.TETags;
import org.confluence.terraentity.init.item.TEBoomerangItems;
import org.confluence.terraentity.init.item.TESummonItems;
import org.confluence.terraentity.init.item.TEWhipItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;

public class TEItemTagsProvider extends ItemTagsProvider {
    public TEItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> b, @Nullable ExistingFileHelper helper) {
        super(output, provider, b, MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        TEBoomerangItems.ITEMS.getEntries().forEach(item -> {
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
            tag(TETags.Items.BOOMERANG_ENCHANTABLE).add(item.get());
        });
        TEWhipItems.ITEMS.getEntries().forEach(item -> {
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
            tag(TETags.Items.WHIP_ENCHANTABLE).add(item.get());
            tag(Tags.Items.MELEE_WEAPON_TOOLS).add(item.get()); // 鞭子属于近战武器
        });
        TESummonItems.ITEMS.getEntries().forEach(item ->{
            tag(TETags.Items.WEAPONS);
        });
    }
}
