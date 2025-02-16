package org.confluence.terraentity.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terraentity.init.TETags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terraentity.TerraEntity.MODID;


public class TEDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public TEDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider holderLookup) {

        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(TETags.DamageTypes.SUMMONER)
                ;

    }



}
