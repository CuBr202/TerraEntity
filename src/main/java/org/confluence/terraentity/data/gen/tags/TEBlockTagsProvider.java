package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEBlockTagsProvider extends BlockTagsProvider {
    public TEBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TETags.Blocks.NPC_HOUSE_CONSTITUTE)
                .addTag(TETags.Blocks.NPC_HOUSE_CHAIR)
                .addTag(TETags.Blocks.NPC_HOUSE_TABLE)
                .add(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.LIGHT, Blocks.SHROOMLIGHT,
                        Blocks.LANTERN, Blocks.SEA_LANTERN, Blocks.SOUL_LANTERN, Blocks.JACK_O_LANTERN);

        this.tag(TETags.Blocks.NPC_HOUSE_CHAIR)
                .addTag(BlockTags.BEDS);

        this.tag(TETags.Blocks.NPC_HOUSE_TABLE)
                .add(Blocks.CRAFTING_TABLE);

    }

    @Override
    public IntrinsicTagAppender<Block> tag(TagKey<Block> tag){
        return super.tag(tag);
    }
}
